package org.kata.service;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.enums.ContactMediumType;

import java.util.List;

public interface ContactMediumService {

    List<ContactMediumDto> getContactMedium(String icp);

    ContactMediumDto saveContactMedium(ContactMediumDto dto);

    ContactMedium getContactMediumByTypeAndValue(ContactMediumType type, String value);
}
