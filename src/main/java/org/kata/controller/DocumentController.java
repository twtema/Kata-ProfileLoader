package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/document")
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/getActual")
    public ResponseEntity<List<DocumentDto>> getDocument(@RequestParam String icp) {
        return new ResponseEntity<>(documentService.getActualDocuments(icp), HttpStatus.OK);
    }

    @Operation(summary = "Get not actual documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of not actual documents"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getNotActual")
    public ResponseEntity<List<DocumentDto>> getNotActualDocument(@RequestParam String icp) {
        return new ResponseEntity<>(documentService.getNotActualDocuments(icp), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый Document", description = "Сохраняет и возвращает DTO нового документа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<DocumentDto> postDocument(
            @Parameter(description = "DTO Document для создания") @RequestBody DocumentDto dto) {
        return new ResponseEntity<>(documentService.saveDocument(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DocumentsNotFoundException.class)
    public ErrorMessage getDocumentHandler(DocumentsNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
