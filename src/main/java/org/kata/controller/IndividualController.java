package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.IndividualDto;
import org.kata.exception.IndividualNotFoundException;
import org.kata.service.DebtCheckService;
import org.kata.exception.IntrudersDetectionException;
import org.kata.service.IndividualService;
import org.kata.service.IntrudersDetectionService;
import org.kata.service.TerroristDetectionService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/individual")
public class IndividualController {

    private final IndividualService individualService;
    private final DebtCheckService debtCheckService;

    private final IntrudersDetectionService intrudersDetectionService;

    private final TerroristDetectionService terroristDetectionService;


    @Operation(summary = "Получить Individual по ICP", description = "Возвращает DTO Individual по ICP")
    @GetMapping
    public ResponseEntity<IndividualDto> getIndividual(
            @Parameter(description = "ICP Individual") String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(individualService.getIndividual(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(individualService.getIndividual(id, type), HttpStatus.OK);
    }

    @Operation(summary = "Создать нового Individual", description = "Сохраняет и возвращает DTO нового индивида")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Individual успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<IndividualDto> postIndividual(
            @Parameter(description = "DTO Individual для создания") @RequestBody IndividualDto dto) {

        debtCheckService.checkBlackListDocumentsWithExisting(dto);

        intrudersDetectionService.checkIndividual(dto);

        terroristDetectionService.checkIndividual(dto);

        return new ResponseEntity<>(individualService.saveIndividual(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Создать тестового Individual", description = "Сохраняет и возвращает DTO тестового индивида")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Individual успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping("/create")
    public ResponseEntity<IndividualDto> addTestIndividual() {
        return new ResponseEntity<>(individualService.saveIndividual(individualService.buildTestIndividual()), HttpStatus.CREATED);
    }

    @Operation(summary = "Получить Individual по номеру", description = "Возвращает DTO Individual по номеру")
    @GetMapping("/byPhone")
    public ResponseEntity<IndividualDto> individualByPhone(
            @Parameter(description = "Phone Individual") @RequestParam(required = false) String phone) {
        return new ResponseEntity<>(individualService.getIndividualByPhone(phone), HttpStatus.OK);
    }

    @Operation(summary = "Delete an individual by icp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted of Individual"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteIndividual(@RequestParam String icp) {
        System.out.println("controller loader delete icp - " + icp);
        individualService.deleteIndividual(icp);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IndividualNotFoundException.class)
    public ErrorMessage getIndividualHandler(IndividualNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IntrudersDetectionException.class)
    public ErrorMessage getIndividualHandler(IntrudersDetectionException e) {
        return new ErrorMessage(e.getMessage());
    }
}
