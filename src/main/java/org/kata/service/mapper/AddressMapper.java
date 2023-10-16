package org.kata.service.mapper;

import org.kata.controller.dto.AddressDto;
import org.kata.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {

    Address toEntity(AddressDto addressDto);

    AddressDto toDto(Address address);

}
