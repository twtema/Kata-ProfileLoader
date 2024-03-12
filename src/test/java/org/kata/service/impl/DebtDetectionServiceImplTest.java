package org.kata.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.DebtDetectionException;
import org.kata.util.FileUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.kata.util.MapperUtil.deserializeIndividualDto;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtDetectionServiceImplTest {
    @Mock
    BlackListDocuments blackListDocuments;
    @Mock
    BlackListContacts blackListContacts;
    @Mock
    BlackListIndividualBirthDate blackListIndividualBirthDate;


    @InjectMocks
    private DebtDetectionServiceImpl debtDetectionService;
    private IndividualDto testIndividualDto;

    @BeforeEach
    public void setUp() throws IOException {
        String IndividualDtoJson = FileUtil.readFromFileToString("TestDebtIndividual.json");
        testIndividualDto = deserializeIndividualDto(IndividualDtoJson);
    }

    @Test
    void checkIndividualException() {
        when(blackListDocuments.getSeries()).thenReturn(Collections.singletonList("0011"));
        when(blackListDocuments.getNumbers()).thenReturn(Collections.singletonList("333333"));
        when(blackListContacts.getNumbervalue()).thenReturn(Collections.singletonList("+489001001010"));
        when(blackListIndividualBirthDate.getBirthDate()).thenReturn(LocalDate.of(1995, 5, 10));

        Assertions.assertThrows(DebtDetectionException.class, () -> {
            debtDetectionService.checkIndividual(testIndividualDto);
        });
    }

    @Test
    void checkIndividualLogInfo() {
        when(blackListDocuments.getSeries()).thenReturn(Collections.singletonList("0011"));
        when(blackListDocuments.getNumbers()).thenReturn(Collections.singletonList("333333"));

        debtDetectionService.checkIndividual(testIndividualDto);
        assertTrue(testIndividualDto.isUnwantedCustomer());
    }

    @Test
    void isBlackListDocument() {
        List<String> blackListDocumentNumbers = new ArrayList<>();
        blackListDocumentNumbers.add("333333");
        blackListDocumentNumbers.add("222222");

        List<String> blackListDocumentSerials = new ArrayList<>();
        blackListDocumentSerials.add("0011");
        blackListDocumentSerials.add("2222");

        String documentNumber = testIndividualDto.getDocuments().get(0).getDocumentNumber();
        String documentSerial = testIndividualDto.getDocuments().get(0).getDocumentSerial();

        System.out.println("documentNumberIndividual: " + documentNumber + ", documentSerialIndividual: " + documentSerial);
        System.out.println("blacklistDocumentNumber: " + blackListDocumentNumbers + ", blacklistDocumentSerial: " + blackListDocumentSerials);

        assertTrue((blackListDocumentNumbers.contains(testIndividualDto.getDocuments().get(0).getDocumentNumber()))
                && (blackListDocumentSerials.contains(testIndividualDto.getDocuments().get(0).getDocumentSerial())));
    }

    @Test
    void isBlackListContacts() {
        List<String> blackListPhoneNumbers = new ArrayList<>();
        blackListPhoneNumbers.add("+489001001010");
        blackListPhoneNumbers.add("89001654010");

        String phoneNumber = testIndividualDto.getContacts().get(0).getValue();

        System.out.println("phoneNumberIndividual: " + phoneNumber);
        System.out.println("blacklistContact: " + blackListPhoneNumbers);

        assertTrue((blackListPhoneNumbers.contains(testIndividualDto.getContacts().get(0).getValue())));
    }

    @Test
    void isBlackListBirthDate() {
        LocalDate actualDate = LocalDate.of(1995, 5, 10);
        Date individualBirthDate = testIndividualDto.getBirthDate();

        LocalDate birthDate = Instant.ofEpochMilli(individualBirthDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        System.out.println("individualBirthDate: " + birthDate);
        System.out.println("BlacklistIndividualBirthDate " + actualDate);

        assertTrue(birthDate.equals(actualDate));
    }
}