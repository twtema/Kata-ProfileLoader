package org.kata.service;

import org.kata.controller.dto.AvatarDto;

public interface AvatarService {

    AvatarDto getAvatar(String icp);

    AvatarDto saveAvatar(AvatarDto dto);

    AvatarDto getAvatar(String icp, String uuid);

}
