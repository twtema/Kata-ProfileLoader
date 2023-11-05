package org.kata.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.enums.DocumentType;

import java.util.Date;

@Data
@Builder
@Jacksonized
public class DocumentDto {

    private String icp;

    private DocumentType documentType;

    private String documentNumber;

    private String documentSerial;

    private Date issueDate;

    private Date expirationDate;
    private boolean actual;
}
