package org.kata.mapper.setters;


import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.kata.entity.enums.DocumentType;

import java.util.Date;


public class DocumentSetter implements Setter {

    @Override
    public void setEntityFields(Object documentObject) {
        Document document = (Document) documentObject;
        document.setDocumentType(DocumentType.RF_PASSPORT);
        document.setIssueDate(new Date());
        document.setExpirationDate(new Date());
        StringFieldsSetterUtil.set(documentObject);
    }

    @Override
    public void setDtoFields(Object documentDtoObject) {
        DocumentDto documentDto = (DocumentDto) documentDtoObject;
        documentDto.setDocumentType(DocumentType.INN);
        documentDto.setIssueDate(new Date());
        documentDto.setExpirationDate(new Date());
        StringFieldsSetterUtil.set(documentDtoObject);
    }
}
