package org.kata.service;

import org.kata.controller.dto.ContactMediumDto;

import java.util.List;

public interface ContactMediumService {

    List<ContactMediumDto> getContactMedium(String icp);

    ContactMediumDto saveContactMedium(ContactMediumDto dto);

    List<ContactMediumDto> getContactMediumByType(String icp, String type);

    List<ContactMediumDto> getContactMediumByUsage(String icp, String usage);

    List<ContactMediumDto> getContactMediumByTypeAndUsage(String icp, String type, String usage);
}
