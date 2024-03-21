package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.DocumentDto;
import org.kata.exception.DocumentsNotFoundException;
import org.kata.service.DocumentService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.kata.utils.Constants.ConstantsControllersCodes.*;
import static org.kata.utils.Constants.ConstantsControllerStrings.*;
import static org.kata.utils.Constants.ConstantsEndpoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(URI_DOCUMENT)
public class DocumentController {



    private final DocumentService documentService;

    @Operation(summary = GET_DOCUMENT_SUMMARY, description = GET_DOCUMENT_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_200, description = SUCCESSFUL_RETRIEVAL_ALL_DOCUMENTS),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST),
            @ApiResponse(responseCode = CODE_500, description = INTERNAL_SERVER_ERROR_DESCRIPTION)
    })
    @GetMapping
    public ResponseEntity<List<DocumentDto>> getDocument(
            @Parameter(description = ICP_DOCUMENT) String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(documentService.getAllDocuments(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(documentService.getAllDocuments(id, type), HttpStatus.OK);
    }

    @Operation(summary = CREATE_NEW_DOCUMENT, description = CREATE_NEW_DOCUMENT_DESC)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = DOCUMENT_CREATED),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })
    @PostMapping
    public ResponseEntity<DocumentDto> postDocument(
            @Parameter(description = DOCUMENT_DTO_FOR_CREATION_DESCRIPTION)
            @RequestBody DocumentDto dto, HttpServletResponse response) {
        DocumentDto documentDto = documentService.saveDocument(dto);
        response.addHeader(X_DEBUG_INFO, String.format(DOCUMENT_SUCCESSFULLY_SAVED_TO_DB,documentDto.getDocumentType()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentDto);
    }

    @Operation(summary = UPDATE_ACTUAL_STATE_SUMMARY, description = UPDATE_ACTUAL_STATE_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = DOCUMENT_SUCCESSFULLY_DEACTIVATED),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST),
            @ApiResponse(responseCode = CODE_500, description = INTERNAL_SERVER_ERROR_DESCRIPTION)
    })
    @PostMapping(UPDATE_ACTUAL_STATE_ENDPOINT)
    public ResponseEntity<DocumentDto> updateActualState(
            @Parameter(description = DTO_DOCUMENT_FOR_DEACTIVATION_DESCRIPTION)
            @RequestBody DocumentDto dto) {
        return new ResponseEntity<>(documentService.updateDocumentActualState(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DocumentsNotFoundException.class)
    public ErrorMessage getDocumentHandler(DocumentsNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
