package org.kata.controller;

import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.AddressDto;
import org.kata.exception.AddressNotFoundException;
import org.kata.service.AddressService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/address")
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/getActual")
    public ResponseEntity<AddressDto> getAddress(@RequestParam(required = false) String id,
                                                 @RequestParam(required = false) String type) {
        if (id != null && type != null) {
            return new ResponseEntity<>(addressService.getAddress(id, type), HttpStatus.OK);
        } else if (id != null) {
            return new ResponseEntity<>(addressService.getAddress(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    @PostMapping
    public ResponseEntity<AddressDto> postAddress(@RequestBody AddressDto dto) {
        return new ResponseEntity<>(addressService.saveAddress(dto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AddressNotFoundException.class)
    public ErrorMessage getAddressHandler(AddressNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}

