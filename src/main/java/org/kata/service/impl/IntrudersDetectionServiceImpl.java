package org.kata.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.enums.ContactMediumType;
import org.kata.exception.IntrudersDetectionException;
import org.kata.service.IntrudersDetectionService;

@Slf4j
public class IntrudersDetectionServiceImpl implements IntrudersDetectionService {
    private boolean isInvalidPassport(IndividualDto dto) {
        for (DocumentDto documentDto : dto.getDocuments()) {
            if (documentDto.getDocumentSerial().startsWith("00")) {
                log.info("паспорт с данной серией {} не прошел валидацию", documentDto.getDocumentSerial());
                return true;
            }

        }
        return false;
    }

    private boolean isInvalidPhoneNumber(IndividualDto dto) {
        for (ContactMediumDto contactDto : dto.getContacts()) {
            if(contactDto.getType() == ContactMediumType.PHONE) {
                String phoneNumber = contactDto.getValue();
                if (!phoneNumber.startsWith("+7") && !phoneNumber.startsWith("8")) {
                    log.info("номер телефона {} не прошел валидацию", contactDto.getValue());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void checkIndividual(IndividualDto dto) {
        if (isInvalidPassport(dto) | isInvalidPhoneNumber(dto)) {
            throw new IntrudersDetectionException("Обнаружен Злоумышленник!");
        }
    }
}
