package org.kata.service.mapper;

import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IndividualMapper {

    Individual toEntity(IndividualDto individualDto);

    IndividualDto toDto(Individual individual);


}
