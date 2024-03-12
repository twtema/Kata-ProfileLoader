package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.TerroristDetectedException;
import org.kata.service.TerroristDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;


@Service
@Slf4j
@RequiredArgsConstructor
public class TerroristDetectionServiceImpl implements TerroristDetectionService {

    private final BlackListDocuments blackListDocuments;
    private final BlackListContacts blackListContacts;
    private final BlackListIndividualBirthDate blackListIndividualBirthDate;

    public boolean isBlackListDocument(IndividualDto dto) {
        return dto.getDocuments().stream()
                .anyMatch(docDto -> blackListDocuments.getSeries().stream()
                        .anyMatch(seriesBlackList -> docDto.getDocumentSerial().equals(seriesBlackList)
                                && blackListDocuments.getNumbers().stream()
                                .anyMatch(numberBlackList -> docDto.getDocumentNumber().equals(numberBlackList))));
    }

    public boolean isBlackListContacts(IndividualDto dto) {
        return dto.getContacts().stream()
                .anyMatch(contactDto -> blackListContacts.getNumbervalue().stream()
                        .anyMatch(contactBlackList -> contactDto.getValue().equals(contactBlackList)));
    }

    public boolean isBlackListBirthDate(IndividualDto dto) {
        if (dto != null && dto.getBirthDate() != null) {
            LocalDate birthDate = dto.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return birthDate.equals(blackListIndividualBirthDate.getBirthDate());
        }
        return false;
    }

    public void checkIndividual(IndividualDto dto) {
        if (dto != null) {
            boolean isBlackListDocument = isBlackListDocument(dto);
            boolean isBlackListContacts = isBlackListContacts(dto);
            boolean isBlackListBirthDate = isBlackListBirthDate(dto);

            if (isBlackListDocument && isBlackListContacts && isBlackListBirthDate) {
                throw new TerroristDetectedException("ВЫ ТЕРРОРИСТ!!!!!!!!!!!!!!!!!!");
            } else if (isBlackListDocument || isBlackListContacts || isBlackListBirthDate) {
                dto.setUnwantedCustomer(true);
            }
        }
    }
}
