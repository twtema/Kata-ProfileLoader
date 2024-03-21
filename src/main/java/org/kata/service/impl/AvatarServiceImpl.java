package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.AvatarDto;
import org.kata.entity.Avatar;
import org.kata.entity.Individual;
import org.kata.exception.AvatarNotFoundException;
import org.kata.repository.AvatarCrudRepository;
import org.kata.service.AvatarService;
import org.kata.service.IndividualService;
import org.kata.service.mapper.AvatarMapper;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.kata.utils.Constants.*;
import static org.kata.utils.Constants.ConstantsServiceLogs.*;
import static org.kata.utils.Constants.ConstantsServicesErrors.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final AvatarCrudRepository avatarCrudRepository;

    private final IndividualService individualService;

    private final AvatarMapper avatarMapper;

    public AvatarDto getAvatar(String icp) {
        List<Avatar> avatars = getIndividual(icp).getAvatar();

        if (!avatars.isEmpty()) {
            Avatar actualAvatar = avatars.stream()
                    .filter(Avatar::isActual)
                    .findFirst()
                    .orElseThrow(() -> new AvatarNotFoundException(String.format(ERR_AVATAR_WITH_ICP_NOT_FOUND, icp)));
            AvatarDto avatarDto = avatarMapper.toDto(actualAvatar);
            avatarDto.setIcp(icp);
            return avatarDto;
        }

        throw new AvatarNotFoundException(String.format(ERR_NO_AVATAR_FOR_INDIVIDUAL, icp));
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
            log.info(LOG_FOR_ICP_SET_NEW_AVATAR, dto.getIcp(), avatar);
        } else {
            avatar = avatarMapper.toEntity(dto);
            avatar.setUuid(UUID.randomUUID().toString());
            avatar.setActual(true);
            avatar.setIndividual(individual);
            avatar.setUploadDate(dto.getUploadDate());
            avatar.setHex(hex);
            log.info(LOG_FOR_ICP_CREATED_NEW_AVATAR, dto.getIcp(), avatar);
        }

        try {
            avatarCrudRepository.save(avatar);
            log.debug(LOG_SAVED_AVATAR_TO_DATABASE, avatar);
        } catch (Exception e) {
            log.warn(ERR_FAILED_TO_SAVE_AVATAR_TO_THE_DATABASE, e);
        }

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

    public void deleteAvatars(String icp, List<Boolean> flags) {
        List<Avatar> avatars = getIndividual(icp).getAvatar();
        Iterator<Boolean> iterator = flags.listIterator();
        List <Avatar> avatarsToDelete = avatars.stream()
                .filter(ava -> iterator.next())
                .toList();
        avatars.removeAll(avatarsToDelete);
        avatarCrudRepository.deleteAll(avatarsToDelete);
    }

    @Override
    public AvatarDto getAvatar(String icp, String uuid) {
        if (uuid.equals(UUID_STRING_VALUE)) {
            return getAvatar(icp);
        } else {
            throw new IllegalArgumentException(ERR_INVALID_TYPE);
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

}
