package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.enums.ContactMediumType;
import org.kata.entity.enums.ContactMediumUsage;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий контактную среду")
public class ContactMediumDto {

    @Schema(description = "ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Тип контактной среды", example = "EMAIL")
    private ContactMediumType type;

    @Schema(description = "Использование контактной среды", example = "BUSINESS")
    private ContactMediumUsage usage;

    @Schema(description = "Значение", example = "example@mail.ru")
    private String value;
}
