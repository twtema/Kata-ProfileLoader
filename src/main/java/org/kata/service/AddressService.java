package org.kata.service;

import org.kata.controller.dto.AddressDto;

public interface AddressService {

    AddressDto getAddress(String icp);

    AddressDto saveAddress(AddressDto dto);

    AddressDto getAddress(String icp, String uuid);
}
