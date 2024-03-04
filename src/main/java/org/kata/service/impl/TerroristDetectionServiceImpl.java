package org.kata.service.impl;

import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.TerroristDetectedException;
import org.kata.service.TerroristDetectionService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TerroristDetectionServiceImpl implements TerroristDetectionService {


    public void checkIndividual(IndividualDto dto) {
        if (dto != null) {
            BlackListDocuments documents = new BlackListDocuments();
            BlackListContacts contacts = new BlackListContacts();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dto.getBirthDate());
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new BlackListIndividualBirthDate().getBirthDate());

            AtomicBoolean isBlackListDocument = new AtomicBoolean(dto.getDocuments().stream()
                    .anyMatch(docDto -> documents.getSeries().stream()
                            .anyMatch(seriesBlackList -> docDto.getDocumentSerial().equals(seriesBlackList)
                                    && documents.getNumbers().stream()
                                    .anyMatch(nomberBlackList -> docDto.getDocumentNumber().equals(nomberBlackList)))));

            AtomicBoolean isBlackListContacts = new AtomicBoolean(dto.getContacts().stream()
                    .anyMatch(contactDto -> contacts.getNumbervalue().stream()
                            .anyMatch(contactBlackList -> contactDto.getValue().equals(contactBlackList))));

            AtomicBoolean isBlackListBirthDate = new AtomicBoolean(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));

            if (isBlackListBirthDate.get() && isBlackListContacts.get() && isBlackListDocument.get()) {
                throw new TerroristDetectedException("ВЫ ТЕРРОРИСТ!!!!!!!!!!!!!!!!!!");
            } else if (isBlackListBirthDate.get() || isBlackListContacts.get() || isBlackListDocument.get()) {
                dto.setUnwantedCustomer(true);
            }
        }
    }
}
