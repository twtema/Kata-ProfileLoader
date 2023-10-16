package org.kata.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.kata.entity.enums.ContactMediumType;

@Data
@Builder
@Jacksonized
public class ContactMediumDto {

    private String icp;

    private ContactMediumType type;

    private String value;
}
