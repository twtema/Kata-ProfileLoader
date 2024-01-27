package org.kata.service;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.RequestContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.enums.ContactMediumType;

import java.util.List;

public interface ContactMediumService {

    List<ContactMediumDto> getContactMedium(String icp);

    List<ContactMediumDto> getContactMedium(RequestContactMediumDto dto);

    ContactMedium getContactMediumByTypeAndValue(ContactMediumType type, String value);

    ContactMediumDto saveContactMedium(ContactMediumDto dto);
}
