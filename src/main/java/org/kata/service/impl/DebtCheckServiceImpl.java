package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.BlackListDocument;
import org.kata.entity.enums.BlackListDocumentType;
import org.kata.exception.BlacklistDocumentException;
import org.kata.repository.DebtCheckCrudRepository;
import org.kata.service.DebtCheckService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DebtCheckServiceImpl implements DebtCheckService {
    private final DebtCheckCrudRepository debtCheckCrudRepository;

    public void saveBlackListDocument(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            BlackListDocument blackList = new BlackListDocument();
            blackList.setUuid(UUID.randomUUID().toString());
            blackList.setBlackListDocumentType(BlackListDocumentType.values()[random.nextInt(BlackListDocumentType.values().length)]);
            blackList.setDocumentNumber(generateRandomDocumentNumber());
            blackList.setDocumentSerial(generateRandomDocumentSerial());
            blackList.setActual(true);

            debtCheckCrudRepository.save(blackList);
        }
    }

    private String generateRandomDocumentNumber() {
        Random random = new Random();
        return String.format("%08d", random.nextInt(100000000));
    }

    private String generateRandomDocumentSerial() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    public void checkBlackListDocumentsWithExisting(IndividualDto dto) {
        List<BlackListDocument> blackListDocuments = debtCheckCrudRepository.findAll().stream()
                .toList();
        List<DocumentDto> documentDto = dto.getDocuments();
        for (BlackListDocument blackListDocument : blackListDocuments) {
            if (documentDto.stream().anyMatch(existingDocument -> isBlacklisted(blackListDocument, existingDocument))) {
                throw new BlacklistDocumentException(dto.getFullName() + " потенциально нежелательный клиент, обратитесь к Диане для введения нового статуса!");
            }
        }
    }

    private boolean isBlacklisted(BlackListDocument blackListDocument, DocumentDto documentDto) {
        return blackListDocument.getDocumentNumber().equals(documentDto.getDocumentNumber()) &&
                blackListDocument.getDocumentSerial().equals(documentDto.getDocumentSerial());
    }
}
