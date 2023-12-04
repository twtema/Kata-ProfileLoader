package org.kata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.exception.JsonConvertException;
import org.kata.service.ContactMediumService;
import org.kata.utils.Converter;
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
            description= "Возвращает DTO ContactMedium по ICP")
    @GetMapping
    public ResponseEntity<List<ContactMediumDto>> getContactMedium(
            @Parameter(description = "ICP ContactMedium") @RequestParam String icp) {
        return new ResponseEntity<>(contactMediumService.getContactMedium(icp), HttpStatus.OK);
    }

    @Hidden
    @GetMapping(value="/data")
    public ResponseEntity<List<String>> codeContactMedium(
            @Parameter(description = "ICP ContactMedium") @RequestParam String data) throws JsonProcessingException {
        Converter<ContactMediumDto> contactMediumDtoConverter = new Converter<>(ContactMediumDto.class);
        List<ContactMediumDto> contactMediumDtosList = contactMediumService.getContactMedium(
                contactMediumDtoConverter.encodeData(data).getIcp());
        List<String> responseString = contactMediumDtosList.stream()
                .map(item -> {
                    try {
                        return contactMediumDtoConverter.codeData(item);
                    } catch (JsonProcessingException e) {
                        throw new JsonConvertException(e.getMessage());
                    }
                }).toList();
        return new ResponseEntity<>(responseString, HttpStatus.OK);
    }

    @Operation(summary = "Создать новый ContactMedium", description = "Сохраняет и возвращает DTO нового контакта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ContactMedium успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<ContactMediumDto> postContactMedium(
            @Parameter(description = "DTO ContactMedium для создания") @RequestBody ContactMediumDto dto) {
        return new ResponseEntity<>(contactMediumService.saveContactMedium(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContactMediumNotFoundException.class)
    public ErrorMessage getContactMediumHandler(ContactMediumNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

}
