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


@Service
@Slf4j
@RequiredArgsConstructor
public class DebtDetectionServiceImpl implements DebtDetectionService {

    private final BlackListDocuments blackListDocuments;
    private final BlackListContacts blackListContacts;
    private final BlackListIndividualBirthDate blackListIndividualBirthDate;

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

    public boolean isBlackListDocument(IndividualDto dto) {
        if (dto != null && dto.getDocuments() != null) {
            return dto.getDocuments().stream()
                    .anyMatch(docDto -> blackListDocuments.getSeries().contains(docDto.getDocumentSerial())
                            && blackListDocuments.getNumbers().contains(docDto.getDocumentNumber()));
        }
        return false;
    }

    public boolean isBlackListContacts(IndividualDto dto) {
        if (dto != null && dto.getContacts() != null) {
            return dto.getContacts().stream()
                    .anyMatch(contactDto -> blackListContacts.getNumbervalue().contains(contactDto.getValue()));
        }
        return false;
    }

    public boolean isBlackListBirthDate(IndividualDto dto) {
        if (dto != null && dto.getBirthDate() != null) {
            LocalDate birthDate = dto.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return birthDate.equals(blackListIndividualBirthDate.getBirthDate());
        }
        return false;
    }
}
