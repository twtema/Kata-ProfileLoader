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
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/individual")
public class IndividualController {

    private final IndividualService individualService;

    @Operation(summary = "Получить Individual по ICP", description = "Возвращает DTO Individual по ICP")
    @GetMapping("/getActual")
    public ResponseEntity<IndividualDto> getIndividual(
            @Parameter(description = "ICP Individual") @RequestParam(required = false) String id,
            @RequestParam(required = false) String type) {
        if (id != null && type != null) {
            return new ResponseEntity<>(individualService.getIndividual(id, type), HttpStatus.OK);
        } else if (id != null) {
            return new ResponseEntity<>(individualService.getIndividual(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IndividualNotFoundException.class)
    public ErrorMessage getIndividualHandler(IndividualNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
