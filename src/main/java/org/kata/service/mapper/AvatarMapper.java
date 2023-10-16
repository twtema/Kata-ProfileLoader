package org.kata.service.mapper;

import org.kata.controller.dto.AvatarDto;
import org.kata.entity.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvatarMapper {

    Avatar toEntity(AvatarDto avatarDto);

    AvatarDto toDto(Avatar avatar);

}
