package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий Адрес")
public class AddressDto {

    @Schema(description = "ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Улица", example = "Пушкина")
    private String street;

    @Schema(description = "Город", example = "Москва")
    private String city;

    @Schema(description = "Государство", example = "Российская Федерация")
    private String state;

    @Schema(description = "Почтовый индекс", example = "101000")
    private String postCode;

    @Schema(description = "Страна", example = "Россия")
    private String country;

}
