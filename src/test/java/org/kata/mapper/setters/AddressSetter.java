package org.kata.mapper.setters;

import org.kata.controller.dto.AddressDto;
import org.kata.entity.Address;

public class AddressSetter implements Setter {


    @Override
    public void setEntityFields(Object addressObject) {
        Address address = (Address) addressObject;
        StringFieldsSetterUtil.set(addressObject);
    }

    @Override
    public void setDtoFields(Object addressDtoObject) {
        AddressDto addressDto = (AddressDto) addressDtoObject;
        StringFieldsSetterUtil.set(addressDtoObject);
    }
}
