package org.kata.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.exception.IntrudersDetectionException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FraudstersDetectionServiceImplTest {

    private BlackListContacts blackListContacts;
    private FraudstersDetectionServiceImpl fraudstersDetectionService;
    private ContactMediumDto contactMediumDto;


    @BeforeEach
    void setUp() {
        blackListContacts = new BlackListContacts();
        blackListContacts.setNumbervalue(Collections.singletonList("89008005050"));
        contactMediumDto = ContactMediumDto.builder().build();
        fraudstersDetectionService = new FraudstersDetectionServiceImpl(blackListContacts);
    }

    @Test
    void testCheckIndividual() {
        IndividualDto individualDto = IndividualDto.builder().build();
        contactMediumDto.setValue("89008005050");
        individualDto.setContacts(Collections.singletonList(contactMediumDto));
        assertThrows(IntrudersDetectionException.class, () -> fraudstersDetectionService.checkIndividual(individualDto));

    }

    @Test
    void testCheckIndividualWithNotProblemsContacts() {
        IndividualDto individualDto = IndividualDto.builder().build();
        contactMediumDto.setValue("89009999999");
        individualDto.setContacts(Collections.singletonList(contactMediumDto));
        assertDoesNotThrow(() -> fraudstersDetectionService.checkIndividual(individualDto));

    }

    @Test
    void testCheckContact() {
        contactMediumDto.setValue("89008005050");
        assertThrows(IntrudersDetectionException.class, (() -> fraudstersDetectionService.checkContact(contactMediumDto)));
    }

    @Test
    void testCheckContactWithNotProblemsContacts() {
        contactMediumDto.setValue("89009999999");
        assertDoesNotThrow(() -> fraudstersDetectionService.checkContact(contactMediumDto));
    }
}