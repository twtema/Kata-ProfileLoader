package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO запроса контактной среды")
public class RequestContactMediumDto {

    @Schema(description = "ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Тип контактной среды", example = "EMAIL")
    private String type;

    @Schema(description = "Использование контактной среды", example = "BUSINESS")
    private String usage;
}
