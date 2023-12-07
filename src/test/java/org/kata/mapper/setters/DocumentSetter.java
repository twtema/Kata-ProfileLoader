package org.kata.mapper.setters;


import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.kata.entity.enums.DocumentType;

import java.util.Date;


public class DocumentSetter implements Setter<Document, DocumentDto> {

    @Override
    public void setEntityFields(Document document) {
        document.setDocumentType(DocumentType.RF_PASSPORT);
        document.setIssueDate(new Date());
        document.setExpirationDate(new Date());
        StringFieldsSetterUtil.set(document);
    }

    @Override
    public void setDtoFields(DocumentDto documentDto) {
        documentDto.setDocumentType(DocumentType.INN);
        documentDto.setIssueDate(new Date());
        documentDto.setExpirationDate(new Date());
        StringFieldsSetterUtil.set(documentDto);
    }
}
