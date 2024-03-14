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

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

import static org.kata.controller.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(URI_WALLET)
public class WalletController {
    private final WalletService walletService;

    @Operation(summary = GET_WALLET_SUMMARY, description= GET_WALLET_DESCRIPTION)
    @GetMapping
    public ResponseEntity<List<WalletDto>> getWallet(
            @Parameter(description = ICP_WALLET_DESCRIPTION) @RequestParam String icp) {
        return new ResponseEntity<>(walletService.getWallet(icp), HttpStatus.OK);
    }

    @Operation(summary = CREATE_WALLET_SUMMARY, description = CREATE_WALLET_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = WALLET_CREATED_DESCRIPTION),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })
    @PostMapping
    public ResponseEntity<WalletDto> postWallet(
            @Parameter(description = DTO_WALLET_FOR_CREATION_DESCRIPTION) @RequestBody WalletDto dto, HttpServletResponse response) {
            WalletDto walletDto = walletService.saveWallet(dto);
            response.addHeader(X_DEBUG_INFO, String.format(WALLET_SUCCESSFULLY_SAVED, walletDto.getWalletId()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(walletDto);
    }
    @Operation(summary = GET_WALLET_BY_MOBILE_AND_CURRENCY_SUMMARY, description = GET_WALLET_BY_MOBILE_AND_CURRENCY_DESCRIPTION)
    @ApiResponses(value = {
    })
    @GetMapping(BY_MOBILE_AND_CURRENCY_ENDPOINT)
    public ResponseEntity<WalletDto> getWalletByMobileAndCurrency(String mobile, CurrencyType currency) {
        return new ResponseEntity<>(walletService.getWalletByMobileAndCurrency(mobile, currency), HttpStatus.OK);
    }

    @Operation(summary = UPDATE_WALLET_SUMMARY, description = UPDATE_WALLET_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_202, description = WALLET_SUCCESSFULLY_UPDATED_DESCRIPTION),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
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
