package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.RequestContactMediumDto;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.service.ContactMediumService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/contactMedium")
public class ContactMediumController {

    private final ContactMediumService contactMediumService;

    @Operation(summary = "Получить ContactMedium по ICP", description = "Возвращает DTO ContactMedium по ICP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ContactMedium успешно получен"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @GetMapping
    public ResponseEntity<List<ContactMediumDto>> getContactMedium(
            @Parameter(description = "DTO ContactMedium для запроса") RequestContactMediumDto dto) {
        return new ResponseEntity<>(contactMediumService.getContactMedium(dto), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый ContactMedium", description = "Сохраняет и возвращает DTO нового контакта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ContactMedium успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<ContactMediumDto> postContactMedium(
            @Parameter(description = "DTO ContactMedium для создания")
            @RequestBody ContactMediumDto dto) {
        return new ResponseEntity<>(contactMediumService.saveContactMedium(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContactMediumNotFoundException.class)
    public ErrorMessage getContactMediumHandler(ContactMediumNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

}
