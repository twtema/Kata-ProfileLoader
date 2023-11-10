package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.WalletDto;
import org.kata.exception.WalletNotFoundException;
import org.kata.service.WalletService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/wallet")
public class WalletController {
    private final WalletService walletService;

    @Operation(summary = "Получить Wallet по icp",
            description= "Возвращает DTO Wallet по ICP")
    @GetMapping
    public ResponseEntity<List<WalletDto>> getDocument(
            @Parameter(description = "ICP Wallet") @RequestParam String icp) {
        return new ResponseEntity<>(walletService.getWallet(icp), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый Wallet", description = "Сохраняет и возвращает DTO нового кошелька")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<WalletDto> postDocument(
            @Parameter(description = "DTO Wallet для создания") @RequestBody WalletDto dto) {
        return new ResponseEntity<>(walletService.saveWallet(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WalletNotFoundException.class)
    public ErrorMessage getWalletHandler(WalletNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
