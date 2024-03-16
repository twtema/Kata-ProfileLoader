package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий банковскую карту")
public class BankCardDto {

    @Schema(description = "ICP владельца", example = "1234567890")
    private String icp;

    @Schema(description = "Имя владельца", example = "Иван")
    private String holderName;

    @Schema(description = "Номер карты", example = "2202201840607718")
    private String cardNumber;

    @Schema(description = "Код проверки подлинности банковской карты", example = "111")
    private int cvv;

    @Schema(description = "Дата окончания срока", example = "01.01.2027")
    private Date expirationDate;

    private boolean actual;
}
