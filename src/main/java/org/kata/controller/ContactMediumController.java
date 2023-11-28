package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.service.ContactMediumService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/contactMedium")
public class ContactMediumController {

    private final ContactMediumService contactMediumService;

    @Operation(summary = "Получить ContactMedium по icp",
            description = "Возвращает DTO ContactMedium по ICP")

    @GetMapping("/getActual")
    public ResponseEntity<List<ContactMediumDto>> getContactMedium(
            @Parameter(description = "ICP ContactMedium")
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String type) {

        if (id != null && type != null) {
            return new ResponseEntity<>(contactMediumService.getContactMedium(id, type), HttpStatus.OK);
        } else if (id != null) {
            return new ResponseEntity<>(contactMediumService.getContactMedium(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
