package org.kata.service.impl;

import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.TerroristDetectedException;
import org.kata.service.TerroristDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;


@Service
public class TerroristDetectionServiceImpl implements TerroristDetectionService {

    public boolean isBlackListDocument(IndividualDto dto) {
        BlackListDocuments documents = new BlackListDocuments();
        return dto.getDocuments().stream()
                .anyMatch(docDto -> documents.getSeries().stream()
                        .anyMatch(seriesBlackList -> docDto.getDocumentSerial().equals(seriesBlackList)
                                && documents.getNumbers().stream()
                                .anyMatch(nomberBlackList -> docDto.getDocumentNumber().equals(nomberBlackList))));
    }

    public boolean isBlackListContacts(IndividualDto dto) {
        BlackListContacts contacts = new BlackListContacts();
        return dto.getContacts().stream()
                .anyMatch(contactDto -> contacts.getNumbervalue().stream()
                        .anyMatch(contactBlackList -> contactDto.getValue().equals(contactBlackList)));
    }

    public boolean isBlackListBirthDate(IndividualDto dto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dto.getBirthDate());
        LocalDate birthDate = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate blackListBirthDate = new BlackListIndividualBirthDate().getBirthDate();
        return birthDate.equals(blackListBirthDate);
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
