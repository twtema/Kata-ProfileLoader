package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.AvatarDto;
import org.kata.entity.Avatar;
import org.kata.entity.Individual;
import org.kata.exception.AvatarNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.AvatarCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.AvatarService;
import org.kata.service.mapper.AvatarMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final AvatarCrudRepository avatarCrudRepository;

    private final IndividualCrudRepository individualCrudRepository;

    private final AvatarMapper avatarMapper;

    public AvatarDto getAvatar(String icp) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<Avatar> avatars = individual.get().getAvatar();

            if (!avatars.isEmpty()) {
                Avatar actualAvatar = avatars.stream()
                        .filter(Avatar::isActual)
                        .findFirst()
                        .orElseThrow(() -> new AvatarNotFoundException("Avatar with icp: " + icp + " not found"));
                AvatarDto avatarDto = avatarMapper.toDto(actualAvatar);
                avatarDto.setIcp(icp);
                return avatarDto;
            } else {
                throw new AvatarNotFoundException("No avatar found for individual with icp: " + icp);
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + icp + " not found");
        }
    }


    public AvatarDto saveAvatar(AvatarDto dto) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());

        return individual.map(ind -> {
            List<Avatar> avatars = ind.getAvatar();
            markAvatarAsNotActual(avatars);

            Avatar avatar = avatarMapper.toEntity(dto);
            avatar.setUuid(UUID.randomUUID().toString());
            avatar.setActual(true);
            avatar.setIndividual(ind);

            log.info("For icp {} created new Avatar: {}", dto.getIcp(), avatar);

            avatarCrudRepository.save(avatar);

            AvatarDto avatarDto = avatarMapper.toDto(avatar);
            avatarDto.setIcp(dto.getIcp());
            return avatarDto;
        }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
    }

    private void markAvatarAsNotActual(List<Avatar> list) {
        list.forEach(avatars -> {
            if (avatars.isActual()) {
                avatars.setActual(false);
            }
        });
    }
}
