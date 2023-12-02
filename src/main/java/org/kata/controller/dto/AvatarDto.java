package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий Avatar")
public class AvatarDto {

    @Schema(description = "ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Имя файла", example = "имя файла")
    private String filename;

    @Schema(description = "Данные изображения")
    private byte[] imageData;

}
