package org.kata.controller;

import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.AddressDto;
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

    @GetMapping("/getActual")
    public ResponseEntity<AvatarDto> getAddress(@RequestParam(required = false) String id,
                                                @RequestParam(required = false) String type) {
        if (id != null && type != null) {
            return new ResponseEntity<>(avatarService.getAvatar(id, type), HttpStatus.OK);
        } else if (id != null) {
            return new ResponseEntity<>(avatarService.getAvatar(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
