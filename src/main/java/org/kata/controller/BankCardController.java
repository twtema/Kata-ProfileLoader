package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.BankCardDto;
import org.kata.exception.BankCardNotFoundException;
import org.kata.service.BankCardService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/bankCard")
public class BankCardController {

    private final BankCardService bankCardService;

    @Operation(summary = "Получить Банковскую карту по icp",
            description = "Возвращает DTO BankCard по ICP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval all bankCards"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<BankCardDto>> getBankCard(
            @Parameter(description = "ICP BankCard") String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(bankCardService.getAllBankCards(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(bankCardService.getAllBankCards(id, type), HttpStatus.OK);
    }

    @Operation(summary = "Создать новую BankCard", description = "Сохраняет и возвращает DTO новой банковской карты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "BankCard успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<BankCardDto> postBankCard(
            @Parameter(description = "DTO BankCard для создания")
            @RequestBody BankCardDto dto, HttpServletResponse response) {
        BankCardDto bankCardDto = bankCardService.saveBankCard(dto);
        response.addHeader("X-Debug-Info", bankCardDto.getBankCardType() + " successfully saved to the database!");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankCardDto);
    }

//    @Operation(
//            summary = "Деактивация актуального документа",
//            description = "Деактивирует актуальный документ если более новый есть в топике Kafka")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Document успешно деактивирован"),
//            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    @PostMapping("/updateActualState")
//    public ResponseEntity<DocumentDto> updateActualState(
//            @Parameter(description = "DTO Document для деактивации")
//            @RequestBody DocumentDto dto) {
//        return new ResponseEntity<>(documentService.updateDocumentActualState(dto), HttpStatus.CREATED);
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BankCardNotFoundException.class)
    public ErrorMessage getBankCardHandler(BankCardNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
