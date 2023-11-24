package org.kata.service;

import org.kata.controller.dto.AddressDto;
import org.kata.entity.Individual;

import java.util.Optional;

public interface AddressService {

    AddressDto getAddress(String uuid);

    AddressDto saveAddress(AddressDto dto);

    AddressDto getAddress(String id, String type);
}
