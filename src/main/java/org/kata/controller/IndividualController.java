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

import javax.servlet.http.HttpServletResponse;

import static org.kata.utils.Constants.ConstantsControllersCodes.*;
import static org.kata.utils.Constants.ConstantsEndpoints.*;
import static org.kata.utils.Constants.ConstantsControllerStrings.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(URI_INDIVIDUAL)
public class IndividualController {

    private final IndividualService individualService;

    @Operation(summary = GET_INDIVIDUAL_SUMMARY, description = GET_INDIVIDUAL_DESCRIPTION)
    @GetMapping
    public ResponseEntity<IndividualDto> getIndividual(
            @Parameter(description = ICP_INDIVIDUAL) String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(individualService.getIndividual(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(individualService.getIndividual(id, type), HttpStatus.OK);
    }

    @Operation(summary = CREATE_INDIVIDUAL_SUMMARY, description = CREATE_INDIVIDUAL_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = INDIVIDUAL_CREATED_DESCRIPTION),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })
    @PostMapping
    public ResponseEntity<IndividualDto> postIndividual(
            @Parameter(description = INDIVIDUAL_TO_CREATE_DESCRIPTION) @RequestBody IndividualDto dto, HttpServletResponse response) {
        IndividualDto individualDto = individualService.saveIndividual(dto);
        response.addHeader(X_DEBUG_INFO, String.format(INDIVIDUAL_SUCCESSFULLY_SAVED, individualDto.getIcp()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(individualDto);
    }

    @Operation(summary = CREATE_TEST_INDIVIDUAL_SUMMARY, description = CREATE_TEST_INDIVIDUAL_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = TEST_INDIVIDUAL_CREATED),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })
    @PostMapping(CREATE_ENDPOINT)
    public ResponseEntity<IndividualDto> addTestIndividual() {
        return new ResponseEntity<>(individualService.saveIndividual(individualService.buildTestIndividual()), HttpStatus.CREATED);
    }

    @Operation(summary = GET_INDIVIDUAL_BY_PHONE_SUMMARY, description = GET_INDIVIDUAL_BY_PHONE_DESCRIPTION)
    @GetMapping(BY_PHONE_ENDPOINT)
    public ResponseEntity<IndividualDto> individualByPhone(
            @Parameter(description = PHONE_INDIVIDUAL) @RequestParam(required = false) String phone) {
        return new ResponseEntity<>(individualService.getIndividualByPhone(phone), HttpStatus.OK);
    }

    @Operation(summary = DELETE_INDIVIDUAL_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_200, description = SUCCESSFUL_DELETION_DESCRIPTION),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST),
            @ApiResponse(responseCode = CODE_500, description = INTERNAL_SERVER_ERROR_DESCRIPTION)
    })
    @DeleteMapping(DELETE_ENDPOINT)
    public ResponseEntity<HttpStatus> deleteIndividual(@RequestParam String icp) {
        System.out.println(String.format(CONTROLLER_LOADER_DELETE_ICP, icp));
        individualService.deleteIndividual(icp);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IndividualNotFoundException.class)
    public ErrorMessage getIndividualHandler(IndividualNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
