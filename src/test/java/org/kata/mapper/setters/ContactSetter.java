package org.kata.mapper.setters;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.enums.ContactMediumType;


public class ContactSetter implements Setter {

    @Override
    public void setEntityFields(Object contactMediumObject) {
        ContactMedium contactMedium = (ContactMedium) contactMediumObject;
        contactMedium.setType(ContactMediumType.PHONE);
        StringFieldsSetterUtil.set(contactMediumObject);
    }

    @Override
    public void setDtoFields(Object contactMediumDtoObject) {
        ContactMediumDto contactMediumDto = (ContactMediumDto) contactMediumDtoObject;
        contactMediumDto.setType(ContactMediumType.EMAIL);
        StringFieldsSetterUtil.set(contactMediumDtoObject);
    }
}
