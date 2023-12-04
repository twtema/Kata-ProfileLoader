package org.kata.service;

import org.kata.controller.dto.ContactMediumDto;

import java.util.List;

public interface ContactMediumService {

    List<ContactMediumDto> getContactMedium(String icp);

    ContactMediumDto saveContactMedium(ContactMediumDto dto);

}
