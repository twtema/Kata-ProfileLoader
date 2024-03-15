package org.kata.service.impl;

import org.junit.jupiter.api.Test;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.enums.ContactMediumType;
import org.kata.service.IntrudersDetectionService;
import org.kata.service.impl.IntrudersDetectionServiceImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntrudersDetectionServiceImplTest {

    private final IntrudersDetectionService detectionService = new IntrudersDetectionServiceImpl();

    @Test
    public void testValidPhoneNumber() {
        IndividualDto dto = IndividualDto.builder().contacts(Collections.singletonList(ContactMediumDto.builder()
                .type(ContactMediumType.PHONE)
                .value("+79123456789")
                        .value("89123456789")
                .build())).build();

        assertFalse(detectionService.isInvalidPhoneNumber(dto));
    }

    @Test
    public void testInvalidPhoneNumber() {
        IndividualDto dto = IndividualDto.builder().contacts(Collections.singletonList(ContactMediumDto.builder()
                .type(ContactMediumType.PHONE)
                .value("123456789")
                .build())).build();

        assertTrue(detectionService.isInvalidPhoneNumber(dto));
    }

    @Test
    public void testValidPassportSerial() {
        IndividualDto dto = IndividualDto.builder().documents(Collections.singletonList(DocumentDto.builder()
                .documentSerial("1234")
                .build())).build();

        assertFalse(detectionService.isInvalidPassport(dto));
    }

    @Test
    public void testInvalidPassportSerial() {
        IndividualDto dto = IndividualDto.builder().documents(Collections.singletonList(DocumentDto.builder()
                .documentSerial("0012")
                .build())).build();

        assertTrue(detectionService.isInvalidPassport(dto));
    }
}
