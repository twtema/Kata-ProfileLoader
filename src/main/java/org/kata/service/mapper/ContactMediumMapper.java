package org.kata.service.mapper;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContactMediumMapper {

    ContactMedium toEntity(ContactMediumDto contactMediumDto);

    ContactMediumDto toDto(ContactMedium contactMedium);

    List<ContactMediumDto> toDto(List<ContactMedium> contactMedium);

}
