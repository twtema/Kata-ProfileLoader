package org.kata.service.mapper;

import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface DocumentMapper {

    Document toEntity(DocumentDto documentDto);

    @Mapping(target = "actual", source = "actual", defaultValue = "false")
    DocumentDto toDto(Document documents);

    @Mapping(target = "actual", source = "actual", defaultValue = "false")
    List<DocumentDto> toDto(List<Document> documents);

}
