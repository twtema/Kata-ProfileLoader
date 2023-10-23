package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.enums.ContactMediumType;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий контактную среду")
public class ContactMediumDto {

    @Schema(description = "ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Тип контактной среды")
    private ContactMediumType type;

    @Schema(description = "Значение")
    private String value;
}
