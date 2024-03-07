package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.DebtDetectionException;
import org.kata.service.DebtDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;


@Service
@Slf4j
@RequiredArgsConstructor
public class DebtDetectionServiceImpl implements DebtDetectionService {

    public void checkIndividual(IndividualDto dto) {
        if (dto != null) {
            boolean isBlackListDocument = isBlackListDocument(dto);
            boolean isBlackListContacts = isBlackListContacts(dto);
            boolean isBlackListBirthDate = isBlackListBirthDate(dto);

            if (isBlackListBirthDate && isBlackListContacts && isBlackListDocument) {
                throw new DebtDetectionException(dto.getFullName() + " является должником!");
            } else if (isBlackListDocument) {
                log.info(dto.getFullName() + " потенциально нежелательный клиент, обратитесь к Диане для введения нового статуса!");
            }
        }
    }
    private boolean isBlackListDocument(IndividualDto dto) {
        BlackListDocuments documents = new BlackListDocuments();
        return dto.getDocuments().stream().anyMatch(docDto -> documents.getSeries().contains(docDto.getDocumentSerial())
                        && documents.getNumbers().contains(docDto.getDocumentNumber()));
    }

    private boolean isBlackListContacts(IndividualDto dto) {
        BlackListContacts contacts = new BlackListContacts();
        return dto.getContacts().stream()
                .anyMatch(contactDto -> contacts.getNumbervalue().contains(contactDto.getValue()));
    }


    private boolean isBlackListBirthDate(IndividualDto dto) {
        Calendar calendarYear = Calendar.getInstance();
        calendarYear.setTime(dto.getBirthDate());
        LocalDate birthDate = calendarYear.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate blackListBirthDate = new BlackListIndividualBirthDate().getBirthDate();
        return birthDate.equals(blackListBirthDate);
    }
}
