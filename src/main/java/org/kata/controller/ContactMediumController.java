package org.kata.controller;

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

    @GetMapping("/getActual")
    public ResponseEntity<List<ContactMediumDto>> getContactMedium(@RequestParam(required = false) String id,
                                                                   @RequestParam(required = false) String type) {
        if (id != null && type != null) {
            return new ResponseEntity<>(contactMediumService.getContactMedium(id, type), HttpStatus.OK);
        } else if (id != null) {
            return new ResponseEntity<>(contactMediumService.getContactMedium(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ContactMediumDto> postContactMedium(@RequestBody ContactMediumDto dto) {
        return new ResponseEntity<>(contactMediumService.saveContactMedium(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContactMediumNotFoundException.class)
    public ErrorMessage getContactMediumHandler(ContactMediumNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

}
