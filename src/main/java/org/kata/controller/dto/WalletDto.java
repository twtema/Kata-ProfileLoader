package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.enums.CurrencyType;

import java.math.BigDecimal;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий кошелёк")
public class WalletDto {

    @Schema(description = "Individual ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Валюта", example = "BYN")
    private CurrencyType currencyType;

    @Schema(description = "Баланс", example = "123.45")
    private BigDecimal balance;

}
