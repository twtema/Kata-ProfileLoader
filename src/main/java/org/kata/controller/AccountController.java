package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.AccountDto;
import org.kata.entity.enums.CurrencyType;
import org.kata.exception.AccountOperationException;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.exception.AccountNotFoundException;
import org.kata.service.AccountService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/account")
public class AccountController {
    private final AccountService accountService;

    private static final String DEPOSIT_OPERATION_SUMMARY = "Производит операции со счетом.";
    private static final String DEPOSIT_OPERATION_DESCRIPTION = "Получает в аргументы только номер счета, объект самой операции получает из redis. Возвращает AccountDto.";

    @Operation(summary = "Получить Account по icp",
            description = "Возвращает DTO Account по ICP")
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccount(
            @Parameter(description = "ICP Account") @RequestParam String icp) {
        return new ResponseEntity<>(accountService.getAccount(icp), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый Account", description = "Сохраняет и возвращает DTO нового счёта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<AccountDto> postAccount(
            @Parameter(description = "DTO Account для создания") @RequestBody AccountDto dto, HttpServletResponse response) {
        AccountDto accountDto = accountService.saveAccount(dto);
        response.addHeader("X-Debug-Info", "Account with ID: " + accountDto.getAccountId() + ", successfully saved to the database!");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountDto);
    }

    @Operation(summary = "Получить Account по номеру телефона и валюте", description = "Возвращает AccountDto")
    @ApiResponses()
    @GetMapping("/byMobileAndCurrency")
    public ResponseEntity<AccountDto> getAccountByMobileAndCurrency(String mobile, CurrencyType currency) {
        return new ResponseEntity<>(accountService.getAccountByMobileAndCurrency(mobile, currency), HttpStatus.OK);
    }

    @Operation(summary = "Обновляет баланс Account по номеру счета", description = "Возвращает DTO обновлённого Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Account успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PatchMapping()
    public ResponseEntity<AccountDto> update(String AccountId, BigDecimal balance) {
        return new ResponseEntity<>(accountService.update(AccountId, balance), HttpStatus.ACCEPTED);
    }

    @Operation(summary = DEPOSIT_OPERATION_SUMMARY, description = DEPOSIT_OPERATION_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "203", description = "Operation Success"),
            @ApiResponse(responseCode = "401", description = "Operation failed")
    })
    @PatchMapping("/operation")
    public ResponseEntity<AccountDto> performAccountOperation(String uuid) throws AccountOperationException {
        return new ResponseEntity<>(accountService.performAccountOperation(uuid), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountNotFoundException.class)
    public ErrorMessage getAccountHandler(AccountNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContactMediumNotFoundException.class)
    public ErrorMessage getContactMediumHandler(ContactMediumNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountOperationException.class)
    public ErrorMessage getAccountOperationExceptionHandler(AccountOperationException e) {
        return new ErrorMessage(e.getMessage());
    }
}