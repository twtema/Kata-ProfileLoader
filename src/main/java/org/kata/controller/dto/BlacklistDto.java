package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kata.entity.enums.DocumentType;

public class BlacklistDto {
    @Schema(description = "ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Тип документа", example = "RF_PASSPORT")
    private DocumentType documentType;

    @Schema(description = "Номер документа", example = "123456")
    private String documentNumber;

    @Schema(description = "Серия документа", example = "1234")
    private String documentSerial;
}
