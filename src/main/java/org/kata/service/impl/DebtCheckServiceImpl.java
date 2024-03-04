package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.BlackListDocument;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.entity.enums.BlackListDocumentType;
import org.kata.exception.TerroristDetectedException;
import org.kata.repository.DebtCheckCrudRepository;
import org.kata.service.DebtCheckService;
import org.kata.service.TerroristDetectionService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class DebtCheckServiceImpl implements TerroristDetectionService {
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
                dto.setUnwantedCustomer(true);
            }

            if (isBlackListDocument.get()) {
                log.info(dto.getFullName() + " потенциально нежелательный клиент, обратитесь к Диане для введения нового статуса!");
            }

        }
    }

//    private final DebtCheckCrudRepository debtCheckCrudRepository;
//
//    public void saveBlackListDocument(int count) {
//        Random random = new Random();
//        for (int i = 0; i < count; i++) {
//            BlackListDocument blackList = new BlackListDocument();
//            blackList.setUuid(UUID.randomUUID().toString());
//            blackList.setBlackListDocumentType(BlackListDocumentType.values()[random.nextInt(BlackListDocumentType.values().length)]);
//            blackList.setDocumentNumber(generateRandomDocumentNumber());
//            blackList.setDocumentSerial(generateRandomDocumentSerial());
//            blackList.setActual(true);
//
//            debtCheckCrudRepository.save(blackList);
//        }
//    }
//
//    private String generateRandomDocumentNumber() {
//        Random random = new Random();
//        return String.format("%08d", random.nextInt(100000000));
//    }
//
//    private String generateRandomDocumentSerial() {
//        Random random = new Random();
//        return String.format("%04d", random.nextInt(10000));
//    }
//
//    public void checkBlackListDocumentsWithExisting(IndividualDto dto) {
//        List<BlackListDocument> blackListDocuments = debtCheckCrudRepository.findAll().stream()
//                .toList();
//        List<DocumentDto> documentDto = dto.getDocuments();
//        for (BlackListDocument blackListDocument : blackListDocuments) {
//            if (documentDto.stream().anyMatch(existingDocument -> isBlacklisted(blackListDocument, existingDocument))) {
//                dto.setUnwantedCustomer(true);
//                log.info(dto.getFullName() + " потенциально нежелательный клиент, обратитесь к Диане для введения нового статуса!");
//                break;
//            }
//        }
//    }
//
//    public boolean isBlacklisted(BlackListDocument blackListDocument, DocumentDto documentDto) {
//        return blackListDocument.getDocumentNumber().equals(documentDto.getDocumentNumber()) &&
//                blackListDocument.getDocumentSerial().equals(documentDto.getDocumentSerial());
//    }
}
