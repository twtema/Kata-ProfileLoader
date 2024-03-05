package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.DebtDetectionException;
import org.kata.service.DebtCheckService;
import org.springframework.stereotype.Service;

import java.util.Calendar;


@Service
@Slf4j
@RequiredArgsConstructor
public class DebtCheckServiceImpl implements DebtCheckService {

    public void checkBlackListDocumentsWithExisting(IndividualDto dto) {
        if (dto != null) {
            BlackListDocuments documents = new BlackListDocuments();
            BlackListContacts contacts = new BlackListContacts();
            Calendar calendarYear = Calendar.getInstance();
            calendarYear.setTime(dto.getBirthDate());
            Calendar calendarMonth = Calendar.getInstance();
            calendarMonth.setTime(new BlackListIndividualBirthDate().getBirthDate());

            boolean isBlackListDocument = dto.getDocuments().stream()
                    .anyMatch(docDto -> documents.getSeries().contains(docDto.getDocumentSerial())
                            && documents.getNumbers().contains(docDto.getDocumentNumber()));

            boolean isBlackListContacts = dto.getContacts().stream()
                    .anyMatch(contactDto -> contacts.getNumbervalue().contains(contactDto.getValue()));

            boolean isBlackListBirthDate = calendarYear.get(Calendar.YEAR) == calendarMonth.get(Calendar.YEAR)
                    && calendarYear.get(Calendar.MONTH) == calendarMonth.get(Calendar.MONTH)
                    && calendarYear.get(Calendar.DAY_OF_MONTH) == calendarMonth.get(Calendar.DAY_OF_MONTH);

            if (isBlackListBirthDate && isBlackListContacts && isBlackListDocument) {
                throw new DebtDetectionException(dto.getFullName() + " является должником!");
            }
            if (isBlackListDocument) {
                log.info(dto.getFullName() + " потенциально нежелательный клиент, обратитесь к Диане для введения нового статуса!");
            }
        }
    }
}
