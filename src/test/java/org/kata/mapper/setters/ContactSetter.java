package org.kata.mapper.setters;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.enums.ContactMediumType;


public class ContactSetter implements Setter<ContactMedium, ContactMediumDto> {

    @Override
    public void setEntityFields(ContactMedium contactMedium) {
        contactMedium.setType(ContactMediumType.PHONE);
        StringFieldsSetterUtil.set(contactMedium);
    }

    @Override
    public void setDtoFields(ContactMediumDto contactMediumDto) {
        contactMediumDto.setType(ContactMediumType.EMAIL);
        StringFieldsSetterUtil.set(contactMediumDto);
    }
}
