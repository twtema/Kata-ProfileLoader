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

import static org.kata.controller.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(URI_AVATAR)
public class AvatarController {

    private final AvatarService avatarService;

    @Operation(summary = GET_AVATAR_BY_ICP,
            description = RETURNS_DTO_AVATAR_BY_ICP)

    @GetMapping
    public ResponseEntity<AvatarDto> getAvatar(
            @Parameter(description = ICP_AVATAR) String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(avatarService.getAvatar(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(avatarService.getAvatar(id, type), HttpStatus.OK);
    }

    @Operation(summary = CREATE_NEW_AVATAR, description = SAVES_AND_RETURNS_NEW_AVATAR_DTO)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = AVATAR_SUCCESSFULLY_CREATED),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })

    @PostMapping
    public ResponseEntity<AvatarDto> postAvatar(@Parameter(description = DTO_AVATAR_FOR_CREATION)
                                                @RequestBody AvatarDto dto, String hex, HttpServletResponse response) {
        AvatarDto avatarDto = avatarService.saveOrUpdateAvatar(dto, hex);
        response.addHeader(X_DEBUG_INFO, String.format(AVATAR_WITH_NAME_SAVED, avatarDto.getFilename()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avatarDto);
    }

    @Operation(summary = GET_AVATAR_LIST_BY_ICP,
            description = RETURNS_LIST_DTO_AVATAR_BY_ICP)
    @GetMapping(ALL_ENDPOINT)
    public ResponseEntity<List<AvatarDto>> getAllAvatars(@Parameter(description = ICP_TO_GET_LIST_AVATAR) @RequestParam String icp) {
        return new ResponseEntity<>(avatarService.getAllAvatarsDto(icp), HttpStatus.OK);
    }

    @Operation(summary = DELETE_AVATARS_BY_ICP_AND_FLAGS, description = DELETE_AVATARS_DESC)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_204, description = AVATAR_SUCCESSFULLY_CREATED),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAvatars(
            @Parameter(description = ICP_FOR_DELETION) @RequestParam String icp,
            @Parameter(description = FLAGS_FOR_DELETION) @RequestParam List<Boolean> flags
    ) {
        avatarService.deleteAvatars(icp, flags);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = UPDATE_ACTIVE_AVATAR, description = UPDATE_AVATAR_DESC)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_202, description = AVATAR_UPDATED_SUCCESS),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })
    @PatchMapping
    public ResponseEntity<AvatarDto> setActiveAvatar(
            @Parameter(description = DTO_FOR_UPDATE) @RequestParam AvatarDto avatarDto,
            @Parameter(description = HASH_OF_UPDATING_IMAGE) @RequestParam String hex
    ) {
        return new ResponseEntity<>(avatarService.saveOrUpdateAvatar(avatarDto, hex), HttpStatus.ACCEPTED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AvatarNotFoundException.class)
    public ErrorMessage getAvatarHandler(AvatarNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}
