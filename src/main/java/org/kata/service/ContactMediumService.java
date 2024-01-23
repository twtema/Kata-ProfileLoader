package org.kata.service;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.RequestContactMediumDto;

import java.util.List;

public interface ContactMediumService {

    List<ContactMediumDto> getContactMedium(String icp);

    List<ContactMediumDto> getContactMedium(RequestContactMediumDto dto);

    ContactMediumDto saveContactMedium(ContactMediumDto dto);

}
