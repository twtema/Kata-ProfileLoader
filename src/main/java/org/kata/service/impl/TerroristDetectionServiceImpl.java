package org.kata.service.impl;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.TerroristDetectedException;
import org.kata.service.TerroristDetectionService;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TerroristDetectionServiceImpl implements TerroristDetectionService {
    public void checkIndividual(IndividualDto dto) {
        if (dto != null) {
            boolean isBlackListDocument = false;
            boolean isBlackListContacts = false;
            boolean isBlackListBirthDate = false;
            BlackListDocuments documents = new BlackListDocuments();
            BlackListContacts contacts = new BlackListContacts();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dto.getBirthDate());
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new BlackListIndividualBirthDate().getBirthDate());

            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH))
                isBlackListBirthDate = true;

            for (DocumentDto docDto : dto.getDocuments()) {
                for (String seriesBlackList : documents.getSeries()) {
                    if (docDto.getDocumentSerial().equals(seriesBlackList)) {
                        for (String nomberBlackList : documents.getNomber()) {
                            if (docDto.getDocumentNumber().equals(nomberBlackList)) {
                                isBlackListDocument = true;
                                break;
                            }
                        }
                    }
                }
            }

            for (ContactMediumDto contactDto : dto.getContacts()) {
                for (String contactBlackList : contacts.getNombervalue()) {
                    if (contactDto.getValue().equals(contactBlackList)) {
                        isBlackListContacts = true;
                        break;
                    }
                }
            }

            if (isBlackListBirthDate && isBlackListContacts && isBlackListDocument)
                throw new TerroristDetectedException("ВЫ ТЕРРОРИСТ!!!!!!!!!!!!!!!!!!");
        }
    }
}
