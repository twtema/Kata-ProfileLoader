package org.kata.mapper;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.kata.entity.enums.DocumentType;
import org.kata.service.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;

@RunWith(SpringRunner.class)
public class DocumentMapperTest implements MapperTest<Document, DocumentDto> {

    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private MapperChecker mapperChecker;

    private Document documentFrom;
    private DocumentDto documentDtoFrom;

    @Before
    public void setUp() {
        documentFrom = new Document();
        documentDtoFrom = documentMapper.toDto(documentFrom);
        setEntityFields(documentFrom);
        setDtoFields(documentDtoFrom);
    }
    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                documentFrom,
                documentMapper.toDto(documentFrom));
    }
    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                documentDtoFrom,
                documentMapper.toEntity(documentDtoFrom));
    }
    @Override
    public void setEntityFields(Document document) {
        document.setDocumentNumber("documentNumberFromDocument");
        document.setDocumentType(DocumentType.RF_PASSPORT);
        document.setIssueDate(new Date());
        document.setExpirationDate(new Date());
        document.setDocumentSerial("documentSerialTest");
    }

    @Override
    public void setDtoFields(DocumentDto documentDto) {
        documentDto.setDocumentNumber("documentNumberFromDocumentDto");
        documentDto.setDocumentType(DocumentType.INN);
        documentDto.setIssueDate(new Date());
        documentDto.setExpirationDate(new Date());
        documentDto.setDocumentSerial("documentSerialTest");
    }
}
