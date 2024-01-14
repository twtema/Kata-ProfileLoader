package org.kata.service;

import org.kata.controller.dto.DocumentDto;
import org.kata.entity.enums.DocumentType;

import java.util.List;

public interface DocumentService {

    List<DocumentDto> getAllDocuments(String icp);

    DocumentDto saveDocument(DocumentDto dto);

    List<DocumentDto> getArchiveDocuments(String icp);

}
