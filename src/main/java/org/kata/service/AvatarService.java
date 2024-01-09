package org.kata.service;

import org.kata.controller.dto.AvatarDto;

import java.util.List;

public interface AvatarService {

    AvatarDto getAvatar(String icp);

    AvatarDto saveOrUpdateAvatar(AvatarDto dto, String hex);

    List<AvatarDto> getAllAvatarsDto(String icp);

    void deleteAvatars(String icp, List<Boolean> flags);
}
