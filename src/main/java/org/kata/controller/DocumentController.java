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

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/document")
public class DocumentController {
    private final DocumentService documentService;

    @Operation(summary = "Получить Document по icp",
            description = "Возвращает DTO Document по ICP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval all documents"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<DocumentDto>> getDocument(
            @Parameter(description = "ICP Document") String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(documentService.getAllDocuments(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(documentService.getAllDocuments(id, type), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый Document", description = "Сохраняет и возвращает DTO нового документа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<DocumentDto> postDocument(
            @Parameter(description = "DTO Document для создания")
            @RequestBody DocumentDto dto, HttpServletResponse response) {
        DocumentDto documentDto = documentService.saveDocument(dto);
        response.addHeader("X-Debug-Info", documentDto.getDocumentType() + " successfully saved to the database!");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentDto);
    }

    @Operation(
            summary = "Деактивация актуального документа",
            description = "Деактивирует актуальный документ если более новый есть в топике Kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document успешно деактивирован"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/updateActualState")
    public ResponseEntity<DocumentDto> updateActualState(
            @Parameter(description = "DTO Document для деактивации")
            @RequestBody DocumentDto dto) {
        return new ResponseEntity<>(documentService.updateDocumentActualState(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DocumentsNotFoundException.class)
    public ErrorMessage getDocumentHandler(DocumentsNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
