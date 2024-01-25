package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO запроса контактной среды")
public class RequestContactMediumDto {

    @Parameter(description = "ICP", required = true)
    private String icp;

    @Parameter(description = "ContactMedium Type", required = false)
    private String type;

    @Parameter(description = "ContactMedium Usage Type", required = false)
    private String usage;
}
