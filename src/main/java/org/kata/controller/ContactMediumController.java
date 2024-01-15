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
    @GetMapping
    public ResponseEntity<List<ContactMediumDto>> getContactMedium(
            @Parameter(description = "ICP ContactMedium") String id,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String usage) {

        if (type == null && usage == null) {
            return new ResponseEntity<>(contactMediumService.getContactMedium(id), HttpStatus.OK);
        } else if (type != null && usage == null) {
            return new ResponseEntity<>(contactMediumService.getContactMediumByType(id, type), HttpStatus.OK);
        } else if (usage != null && type == null) {
            return new ResponseEntity<>(contactMediumService.getContactMediumByUsage(id, usage), HttpStatus.OK);
        }
        return new ResponseEntity<>(contactMediumService.getContactMediumByTypeAndUsage(id, type, usage), HttpStatus.OK);
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
