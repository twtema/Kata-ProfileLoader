package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Gets Avatar by icp",
            description= "Avatar must exist")
    @GetMapping
    public ResponseEntity<AvatarDto> getAvatar(@RequestParam String icp) {
        return new ResponseEntity<>(avatarService.getAvatar(icp), HttpStatus.OK);
    }

    @Operation(summary = "Post Avatar by dto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avatar успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping
    public ResponseEntity<AvatarDto> postAvatar(@RequestBody AvatarDto dto) {
        return new ResponseEntity<>(avatarService.saveAvatar(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AvatarNotFoundException.class)
    public ErrorMessage getAvatarHandler(AvatarNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
