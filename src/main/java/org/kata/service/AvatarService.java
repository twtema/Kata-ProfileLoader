package org.kata.service;

import org.kata.controller.dto.AvatarDto;

public interface AvatarService {

    AvatarDto getAvatar(String uuid);

    AvatarDto saveAvatar(AvatarDto dto);
}
