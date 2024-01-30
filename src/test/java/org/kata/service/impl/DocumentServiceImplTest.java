package org.kata.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kata.controller.dto.DocumentDto;
import org.kata.entity.enums.DocumentType;
import org.kata.exception.IndividualNotFoundException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
public class DocumentServiceImplTest {

    @Autowired
    private DocumentServiceImpl documentService;
    String startDateString = "06/27/2007";
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    Date startDate;

    {
        try {
            startDate = df.parse(startDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
     void testUpdateDocument() {
        String icp = "193-74-1824";
        DocumentDto dto = new DocumentDto();
        dto.setIcp(icp);
        dto.setDocumentType(DocumentType.RF_PASSPORT);
        dto.setDocumentNumber("1234567890");
        dto.setDocumentSerial("1234");
        dto.setIssueDate(startDate);
        dto.setExpirationDate(startDate);
        dto.setExternalDate(Date.from(Instant.now()));

        DocumentDto result = documentService.updateDocument(dto);


        assertEquals("1234", result.getDocumentSerial());
        assertEquals(DocumentType.RF_PASSPORT, result.getDocumentType());
        assertEquals("1234567890", result.getDocumentNumber());
        assertEquals(startDate, result.getIssueDate());
        assertEquals(startDate, result.getExpirationDate());

    }
    @Test
    void individualNotFoundExceptionTest() {
        String icp = "193-741824";
        DocumentDto dto = new DocumentDto();
        dto.setIcp(icp);
        dto.setDocumentType(DocumentType.RF_PASSPORT);
        dto.setDocumentNumber("1234567890");
        dto.setDocumentSerial("1234");
        dto.setIssueDate(startDate);
        dto.setExpirationDate(startDate);
        dto.setExternalDate(Date.from(Instant.now()));

        Assertions.assertThrows(IndividualNotFoundException.class, () -> documentService.updateDocument(dto));
    }

}
