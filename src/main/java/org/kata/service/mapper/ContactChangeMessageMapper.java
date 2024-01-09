package org.kata.service.mapper;

import org.kata.controller.dto.ContactChangeMessageDTO;
import org.kata.entity.ContactChangeMessage;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContactChangeMessageMapper {

    ContactChangeMessage toEntity(ContactChangeMessageDTO contactChangeMessageDTO);

    ContactChangeMessageDTO toDto(ContactChangeMessage contactChangeMessage);
}
