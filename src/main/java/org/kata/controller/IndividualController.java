package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.IndividualDto;
import org.kata.exception.IndividualNotFoundException;
import org.kata.service.IndividualService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/individual")
public class IndividualController {

    private final IndividualService individualService;

    @Operation(summary = "Получить Individual по ICP", description = "Возвращает DTO Individual по ICP")
    @GetMapping
    public ResponseEntity<IndividualDto> getIndividual(
            @Parameter(description = "ICP Individual") @RequestParam String icp) {
        return new ResponseEntity<>(individualService.getIndividual(icp), HttpStatus.OK);
    }

    @Operation(summary = "Создать нового Individual", description = "Сохраняет и возвращает DTO нового индивида")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Individual успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<IndividualDto> postIndividual(
            @Parameter(description = "DTO Individual для создания") @RequestBody IndividualDto dto) {
        return new ResponseEntity<>(individualService.saveIndividual(dto), HttpStatus.CREATED);
    }
    @Operation(summary = "Получить Individual по номеру", description = "Возвращает DTO Individual по номеру")
    @GetMapping("/byPhone")
    public ResponseEntity<IndividualDto> individualByPhone(
            @Parameter(description = "Phone Individual") @RequestParam(required = false) String phone) {
        return new ResponseEntity<>(individualService.getIndividualByPhone(phone), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IndividualNotFoundException.class)
    public ErrorMessage getIndividualHandler(IndividualNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
