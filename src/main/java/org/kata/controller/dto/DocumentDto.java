package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.enums.DocumentType;

import java.util.Date;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий документы")
public class DocumentDto {

    @Schema(description = "ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Тип документа", example = "RF_PASSPORT")
    private DocumentType documentType;

    @Schema(description = "Номер документа", example = "123456")
    private String documentNumber;

    @Schema(description = "Серия документа", example = "1234")
    private String documentSerial;

    @Schema(description = "Дата выдачи", example = "01.01.2010")
    private Date issueDate;

    @Schema(description = "Дата истечения срока действия", example = "null")
    private Date expirationDate;
}
