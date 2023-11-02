package org.kata.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.Individual;
import org.kata.entity.enums.Currency;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющий кошелёк")
public class WalletDto {

    @Schema(description = "Individual ICP", example = "1234567890")
    private String icp;

    @Schema(description = "Валюта", example = "BYN")
    private Currency currency;

    @Schema(description = "Баланс", example = "123.45")
    private BigDecimal value;

}
