package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.WalletDto;
import org.kata.entity.enums.CurrencyType;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.exception.WalletNotFoundException;
import org.kata.service.WalletService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/wallet")
public class WalletController {
    private final WalletService walletService;

    @Operation(summary = "Получить Wallet по icp",
            description= "Возвращает DTO Wallet по ICP")
    @GetMapping
    public ResponseEntity<List<WalletDto>> getWallet(
            @Parameter(description = "ICP Wallet") @RequestParam String icp) {
        return new ResponseEntity<>(walletService.getWallet(icp), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый Wallet", description = "Сохраняет и возвращает DTO нового кошелька")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<WalletDto> postWallet(
            @Parameter(description = "DTO Wallet для создания") @RequestBody WalletDto dto) {
        return new ResponseEntity<>(walletService.saveWallet(dto), HttpStatus.CREATED);
    }
    @Operation(summary = "Получить Wallet по номеру телефона и валюте", description = "Возвращает WalletDto")
    @ApiResponses(value = {
    })
    @GetMapping("/byMobileAndCurrency")
    public ResponseEntity<WalletDto> getWalletByMobileAndCurrency(String mobile, CurrencyType currency) {
        return new ResponseEntity<>(walletService.getWalletByMobileAndCurrency(mobile, currency), HttpStatus.OK);
    }

    @Operation(summary = "Обновляет баланс Wallet по номеру кошелька", description = "Возвращает DTO обновлённого Wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Wallet успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PatchMapping()
    public ResponseEntity<WalletDto> update(String walletId, BigDecimal balance) {
        return new ResponseEntity<>(walletService.update(walletId, balance), HttpStatus.ACCEPTED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WalletNotFoundException.class)
    public ErrorMessage getWalletHandler(WalletNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContactMediumNotFoundException.class)
    public ErrorMessage getContactMediumHandler(ContactMediumNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

}
