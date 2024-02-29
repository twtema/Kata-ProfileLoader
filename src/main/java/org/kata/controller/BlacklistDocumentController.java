package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.service.DebtCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/blacklistDocument")
public class BlacklistDocumentController {
    private final DebtCheckService debtCheckService;
    @Operation(summary = "Создать черный список клиентов", description = "Сохраняет черный список клиентов по номеру и серии документа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avatar успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<String> saveBlackListDocument(@Parameter(description = "Количество записей для создания") @RequestParam int count) {
        debtCheckService.saveBlackListDocument(count);
        return new ResponseEntity<>("Blacklist documents populated successfully", HttpStatus.CREATED);
    }
}
