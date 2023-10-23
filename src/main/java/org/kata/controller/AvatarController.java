package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.AvatarDto;
import org.kata.exception.AvatarNotFoundException;
import org.kata.service.AvatarService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @Operation(summary = "Получить Avatar по icp",
            description= "Возвращает DTO Avatar по ICP")
    @GetMapping
    public ResponseEntity<AvatarDto> getAvatar(
            @Parameter(description = "ICP Avatar") @RequestParam String icp) {
        return new ResponseEntity<>(avatarService.getAvatar(icp), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый Avatar", description = "Сохраняет и возвращает DTO нового аватара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avatar успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<AvatarDto> postAvatar(
            @Parameter(description = "DTO Avatar для создания") @RequestBody AvatarDto dto) {
        return new ResponseEntity<>(avatarService.saveAvatar(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AvatarNotFoundException.class)
    public ErrorMessage getAvatarHandler(AvatarNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
