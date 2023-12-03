package org.kata.mapper.setters;

import org.kata.controller.dto.AddressDto;
import org.kata.entity.Address;

public class AddressSetter implements Setter<Address, AddressDto> {


    @Override
    public void setEntityFields(Address address) {
        StringFieldsSetterUtil.set(address);
    }

    @Override
    public void setDtoFields(AddressDto addressDto) {
        StringFieldsSetterUtil.set(addressDto);
    }
}
