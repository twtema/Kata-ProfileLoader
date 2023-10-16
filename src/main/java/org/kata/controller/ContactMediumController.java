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

    @GetMapping
    public ResponseEntity<List<ContactMediumDto>> getContactMedium(@RequestParam String icp) {
        return new ResponseEntity<>(contactMediumService.getContactMedium(icp), HttpStatus.OK);
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
