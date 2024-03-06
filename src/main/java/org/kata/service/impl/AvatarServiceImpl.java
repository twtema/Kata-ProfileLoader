package org.kata.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.AvatarDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Avatar;
import org.kata.entity.Individual;
import org.kata.exception.AvatarNotFoundException;
import org.kata.repository.AvatarCrudRepository;
import org.kata.service.AvatarService;
import org.kata.service.IndividualService;
import org.kata.service.mapper.AvatarMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class  AvatarServiceImpl implements AvatarService {
    private final AvatarCrudRepository avatarCrudRepository;
    private final IndividualService individualService;
    private final AvatarMapper avatarMapper;
    private final CacheManager cacheManager;

    @Cacheable(key = "#icp", value = "icpAvatar")
    public AvatarDto getAvatar(String icp) {
        List<Avatar> avatars = getIndividual(icp).getAvatar();

        if (!avatars.isEmpty()) {
            Avatar actualAvatar = avatars.stream()
                    .filter(Avatar::isActual)
                    .findFirst()
                    .orElseThrow(() -> new AvatarNotFoundException("Avatar with icp: " + icp + " not found"));
            AvatarDto avatarDto = avatarMapper.toDto(actualAvatar);
            avatarDto.setIcp(icp);
            return avatarDto;
        }

        throw new AvatarNotFoundException("No avatar found for individual with icp: " + icp);
    }


    public AvatarDto saveOrUpdateAvatar(AvatarDto dto, String hex) {
        Individual individual = getIndividual(dto.getIcp());
        List<Avatar> avatars = individual.getAvatar();
        markAvatarAsNotActual(avatars);
        Avatar avatar;
        Optional<Avatar> optAvatar = avatars.stream()
                .filter(ava -> ava.getHex().equals(hex))
                .findFirst();
        if (optAvatar.isPresent()) {
            avatar = optAvatar.get();
            avatar.setActual(true);
            log.info("For icp {} sat new Avatar: {}", dto.getIcp(), avatar);
        } else {
            avatar = avatarMapper.toEntity(dto);
            avatar.setUuid(UUID.randomUUID().toString());
            avatar.setActual(true);
            avatar.setIndividual(individual);
            avatar.setUploadDate(dto.getUploadDate());
            avatar.setHex(hex);
            log.info("For icp {} created new Avatar: {}", dto.getIcp(), avatar);
        }

        avatarCrudRepository.save(avatar);

        addingNewAvatarInCache(dto);

        AvatarDto avatarDto = avatarMapper.toDto(avatar);
        avatarDto.setIcp(dto.getIcp());
        return avatarDto;
    }

    public List<AvatarDto> getAllAvatarsDto(String icp) {
        List<Avatar> avatars = getIndividual(icp).getAvatar();
        if (!avatars.isEmpty()) {
            return avatars.stream().map(avatarMapper::toDto).toList();
        }
        return new ArrayList<>();
    }


    @CacheEvict(key = "#icp", value = {"icpIndividual", "icpAvatar"})
    public void deleteAvatars(String icp, List<Boolean> flags) {
        List<Avatar> avatars = getIndividual(icp).getAvatar();
        Iterator<Boolean> iterator = flags.listIterator();
        List <Avatar> avatarsToDelete = avatars.stream()
                .filter(ava -> iterator.next())
                .toList();
        avatars.removeAll(avatarsToDelete);
        avatarCrudRepository.deleteAll(avatarsToDelete);
        /**
         * This method does not work correctly and needs to be fixed.
         * Until the method is fixed, calling this method will remove avatars and individuals from the cache by @CacheEvict based on ICP.
         *
         * After correcting this method, you need to remove the annotation @CacheEvict.
         * And it is also necessary to prescribe the logic of checking the cache based on cacheManager call.
         */
//        Cache cacheAvatar = cacheManager.getCache("icpAvatar");
//        Cache cacheIndividual = cacheManager.getCache("icpIndividual");
//
//
//         if (cacheAvatar != null && cacheAvatar.get(dto.getIcp()) != null) {
//                *** TO_DO ***
//            }
//
//         if (cacheIndividual != null && cacheIndividual.get(dto.getIcp()) != null) {
//                *** TO_DO ***
//            }
    }

    @Override
    @Cacheable(key = "#icp", value = "icpAvatar")
    public AvatarDto getAvatar(String icp, String uuid) {
        if (uuid.equals("uuid")) {
            return getAvatar(icp);
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    private void markAvatarAsNotActual(List<Avatar> list) {
        list.forEach(avatars -> {
            if (avatars.isActual()) {
                avatars.setActual(false);
            }
        });
    }

    private Individual getIndividual(String icp) {
        return individualService.getIndividualEntity(icp);
    }

    private void addingNewAvatarInCache(AvatarDto dto) {
        Cache cacheAvatar = cacheManager.getCache("icpAvatar");
        Cache cacheIndividual = cacheManager.getCache("icpIndividual");

        if (cacheAvatar != null && cacheAvatar.get(dto.getIcp()) != null) {
            cacheAvatar.put(dto.getIcp(), dto);
        }

        if (cacheIndividual != null && cacheIndividual.get(dto.getIcp()) != null) {
            IndividualDto individualDto = (IndividualDto) cacheIndividual.get(dto.getIcp()).get();
            individualDto.getAvatar().add(dto);
            cacheIndividual.put(dto.getIcp(), individualDto);
        }
    }
}
