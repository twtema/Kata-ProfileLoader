package org.kata.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.service.ContactMediumService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.kata.controller.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(URI_CONTACT_MEDIUM)
public class ContactMediumController {

    private final ContactMediumService contactMediumService;

    @Operation(summary = GET_CONTACT_MEDIUM_SUMMARY,
            description = GET_CONTACT_MEDIUM_DESCRIPTION)

    @GetMapping
    public ResponseEntity<List<ContactMediumDto>> getContactMedium(
            @Parameter(description = ICP_CONTACT_MEDIUM_DESCRIPTION) String id,
            @RequestParam(required = false) String type) {

        if (type == null) {
            return new ResponseEntity<>(contactMediumService.getContactMedium(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(contactMediumService.getContactMedium(id, type), HttpStatus.OK);
    }

    @Operation(summary = CREATE_CONTACT_MEDIUM_SUMMARY, description = CREATE_CONTACT_MEDIUM_DESCRIPTION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_201, description = CONTACT_MEDIUM_CREATED),
            @ApiResponse(responseCode = CODE_400, description = BAD_REQUEST)
    })

    @PostMapping
    public ResponseEntity<ContactMediumDto> postContactMedium(
            @Parameter(description = DTO_CONTACT_MEDIUM_DESCRIPTION)
            @RequestBody ContactMediumDto dto, HttpServletResponse response) {
        ContactMediumDto contactMediumDto = contactMediumService.saveContactMedium(dto);
        response.addHeader(X_DEBUG_INFO, String.format(CONTACT_MEDIUM_SAVED_TO_DB, contactMediumDto.getType()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactMediumDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ContactMediumNotFoundException.class)
    public ErrorMessage getContactMediumHandler(ContactMediumNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

}
