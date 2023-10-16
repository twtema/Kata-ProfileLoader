package org.kata.service.mapper;

import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DocumentMapper {

    Document toEntity(DocumentDto documentDto);

    DocumentDto toDto(Document documents);

    List<DocumentDto> toDto(List<Document> documents);

}
