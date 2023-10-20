package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("v1/address")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Gets Address by icp",
            description= "Address must exist")
    @GetMapping
    public ResponseEntity<AddressDto> getAddress(@RequestParam String icp) {
        return new ResponseEntity<>(addressService.getAddress(icp), HttpStatus.OK);
    }

    @Operation(summary = "Post Address by dto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
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
