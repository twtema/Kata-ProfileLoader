package org.kata.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.exception.IntrudersDetectionException;
import org.kata.service.FraudstersDetectionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FraudstersDetectionServiceImpl implements FraudstersDetectionService {


    private boolean isInvalidPhoneNumber(List<ContactMediumDto> listDto) {
        BlackListContacts contacts = new BlackListContacts();
        return listDto.stream().anyMatch(contactDto -> contacts.getNumbervalue().stream()
                        .anyMatch(contactBlackList -> contactDto.getValue().equals(contactBlackList)));
    }


    @Override
    public void checkIndividual(ContactMediumDto dto) {
        List<ContactMediumDto> contacts = List.of(dto);
        if (isInvalidPhoneNumber(contacts)) {
            throw new IntrudersDetectionException("Обнаружен Мошенник!");
        }
    }

    @Override
    public void checkIndividual(IndividualDto dto) {
        List<ContactMediumDto> contacts = dto.getContacts();
        if (isInvalidPhoneNumber(contacts)) {
            throw new IntrudersDetectionException("Обнаружен Мошенник!");
        }
    }
}
