package org.kata.controller;

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
    public ResponseEntity<List<DocumentDto>> getDocument(@RequestParam(required = false) String id,
                                                         @RequestParam(required = false) String type) {
        if (id != null && type != null) {
            return new ResponseEntity<>(documentService.getDocument(id, type), HttpStatus.OK);
        } else if (id != null) {
            return new ResponseEntity<>(documentService.getDocument(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
