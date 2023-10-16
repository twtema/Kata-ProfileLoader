package org.kata.service;

import org.kata.controller.dto.AddressDto;

public interface AddressService {

    AddressDto getAddress(String uuid);

    AddressDto saveAddress(AddressDto dto);
}
