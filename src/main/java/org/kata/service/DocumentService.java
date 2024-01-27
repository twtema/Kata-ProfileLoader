package org.kata.service;

import org.kata.controller.dto.DocumentDto;

import java.util.List;

public interface DocumentService {

    List<DocumentDto> getAllDocuments(String icp);

    DocumentDto saveDocument(DocumentDto dto);

    List<DocumentDto> getArchiveDocuments(String icp);

    DocumentDto updateDocument(DocumentDto dto);
}
