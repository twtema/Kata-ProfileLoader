package org.kata.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AddressDto {

    private String icp;

    private String street;

    private String city;

    private String state;

    private String postCode;

    private String country;

}
