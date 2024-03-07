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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @Operation(summary = "Получить Avatar по icp",
            description = "Возвращает DTO Avatar по ICP")

    @GetMapping
    public ResponseEntity<AvatarDto> getAvatar(
            @Parameter(description = "ICP Avatar") String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(avatarService.getAvatar(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(avatarService.getAvatar(id, type), HttpStatus.OK);
    }

    @Operation(summary = "Создать новый Avatar", description = "Сохраняет и возвращает DTO нового аватара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avatar успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })

    @PostMapping
    public ResponseEntity<AvatarDto> postAvatar(@Parameter(description = "DTO Avatar для создания")
                                                    @RequestBody AvatarDto dto, String hex, HttpServletResponse response) {
        AvatarDto avatarDto = avatarService.saveOrUpdateAvatar(dto, hex);
        response.addHeader("X-Debug-Info", "Avatar with name: " + avatarDto.getFilename() + ", successfully saved to the database!");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avatarDto);
    }

    @Operation(summary = "Получить список Avatar по icp",
            description= "Возвращает список DTO Avatar по ICP")
    @GetMapping("/all")
    public ResponseEntity<List<AvatarDto>> getAllAvatars(@Parameter(description = "ICP для получения List<Avatar>") @RequestParam String icp) {
        return new ResponseEntity<>(avatarService.getAllAvatarsDto(icp), HttpStatus.OK);
    }

    @Operation(summary = "Запрос на удаление аватаров по icp и списку флагов",
            description = "Запрос на удаление одного или нескольких Avatar по icp и списку boolean (галочки) в соответствии со списком getAllAvatars(String icp)"
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avatar успешно удалён"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAvatars(@Parameter(description = "ICP для удаления") @RequestParam String icp,
                                                    @Parameter(description = "Флаги для удаления") @RequestParam List<Boolean> flags) {
        avatarService.deleteAvatars(icp, flags);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Обновляет активный аватар",
            description= "Обновляет аватар по AvatarDto и хешу, рассчитанному в ProfileAvatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Avatar успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PatchMapping
    public ResponseEntity<AvatarDto> setActiveAvatar(@Parameter(description = "DTO для обновления") @RequestParam AvatarDto avatarDto,
                                                     @Parameter(description = "Хеш обновляемого изображения") @RequestParam String hex){
        return new ResponseEntity<>(avatarService.saveOrUpdateAvatar(avatarDto, hex), HttpStatus.ACCEPTED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AvatarNotFoundException.class)
    public ErrorMessage getAvatarHandler(AvatarNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
