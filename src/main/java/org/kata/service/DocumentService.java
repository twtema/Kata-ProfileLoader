package org.kata.service;

import org.kata.controller.dto.DocumentDto;

import java.util.List;

public interface DocumentService {

    List<DocumentDto> getDocument(String icp);

    DocumentDto saveDocument(DocumentDto dto);

    List <DocumentDto> getDocument (String icp, String uuid);
}
