package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.kata.controller.dto.AddressDto;
import org.kata.exception.AddressNotFoundException;
import org.kata.service.AddressService;
import org.kata.utils.Constants;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static org.kata.utils.Constants.ConstantsControllersCodes.*;
import static org.kata.utils.Constants.ConstantsControllerStrings.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.ConstantsEndpoints.URI_ADDRESS)
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = GET_ADDRESS_BY_ICP, description = RETURNS_DTO_ADDRESS_BY_ICP)
    @GetMapping
    public ResponseEntity<AddressDto> getAddress(
            @Parameter(description = ICP_ADDRESS) String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(addressService.getAddress(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(addressService.getAddress(id, type), HttpStatus.OK);
    }

    @Operation(summary = CREATE_NEW_ADDRESS, description = SAVES_AND_RETURN_NEW_ADDRESS_DTO)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = ADDRESS_CREATED),
            @ApiResponse(responseCode = CODE_400, description = INCORRECT_REQUEST)
    })
    @PostMapping
    public ResponseEntity<AddressDto> postAddress(
            @Parameter(description = DTO_ADDRESS_FOR_CREATE)
            @RequestBody AddressDto dto,
            HttpServletResponse response
    ) {
        AddressDto addressDto = addressService.saveAddress(dto);
        response.addHeader(X_DEBUG_INFO, String.format(ADDRESS_WITH_ICP_SAVED, addressDto.getIcp()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AddressNotFoundException.class)
    public ErrorMessage getAddressHandler(AddressNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }
}

