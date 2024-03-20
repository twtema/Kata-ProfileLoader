package org.kata.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.enums.CurrencyType;
import org.kata.entity.enums.OperationType;

import java.math.BigDecimal;

@Data
@Builder
@Jacksonized
@Schema(description = "DTO представляющее операцию со счетом")
public class AccountOperationDto {

    @Schema(description = "UUID операции", example = "1234567890")
    private String UUID;

    @Schema(description = "Тип операции", example = "DEPOSIT")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OperationType operationType;

    @Schema(description = "Номер счёта клиента", example = "1234567890")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accountId;

    @Schema(description = "Сумма операции", example = "123.45")
    private BigDecimal amount;

    @Schema(description = "Тип валюты", example = "BYN")
    private CurrencyType currencyType;

    @Schema(description = "Второй счет для операции", example = "1234567890")
    private String destinationAccountId;

    @Schema(description = "Счетик операций", example = "2")
    private Integer counterOfOperation;

    @Schema(description = "Одноразовый код", example = "123456")
    private String OTP;

    @Schema(description = "Попытки ввода кода", example = "5")
    private Integer attemptsOfOTP;

}