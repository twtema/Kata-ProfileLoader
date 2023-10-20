package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Gets Document by icp",
            description= "Document must exist")
    @GetMapping
    public ResponseEntity<List<DocumentDto>> getDocument(@RequestParam String icp) {
        return new ResponseEntity<>(documentService.getDocument(icp), HttpStatus.OK);
    }

    @Operation(summary = "Post Document by dto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<DocumentDto> postDocument(@RequestBody DocumentDto dto) {
        return new ResponseEntity<>(documentService.saveDocument(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DocumentsNotFoundException.class)
    public ErrorMessage getDocumentHandler(DocumentsNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
