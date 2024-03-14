package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.DocumentDto;
import org.kata.entity.Document;
import org.kata.entity.Individual;
import org.kata.exception.DocumentsNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.DocumentCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.DocumentService;
import org.kata.service.mapper.DocumentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.kata.service.impl.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentCrudRepository documentCrudRepository;

    private final IndividualCrudRepository individualCrudRepository;

    private final DocumentMapper documentMapper;

    public List<DocumentDto> getAllDocuments(String icp) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<Document> documents = individual.get().getDocuments();

            if (!documents.isEmpty()) {
                List<DocumentDto> documentDtos = documentMapper.toDto(documents);
                documentDtos.forEach(doc -> doc.setIcp(icp));
                documentDtos.forEach(doc -> doc.setActual(doc.isActual()));

                return documentDtos;
            } else {
                throw new DocumentsNotFoundException(String.format(ERROR_NO_DOCUMENT_FOUND_FOR_INDIVIDUAL, icp));
            }
        } else {
            throw new IndividualNotFoundException(String.format(ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND, icp));
        }
    }

    public DocumentDto saveDocument(DocumentDto dto) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());

        return individual.map(ind -> {
            List<Document> documents = ind.getDocuments();
            List<Document> markOldDoc = documents.stream()
                    .filter(doc -> dto.getDocumentType().equals(doc.getDocumentType()))
                    .collect(Collectors.toList());
            markDocumentAsNotActual(markOldDoc);

            Document document = documentMapper.toEntity(dto);
            document.setUuid(UUID.randomUUID().toString());
            document.setActual(true);
            document.setIndividual(ind);

            log.info(LOG_FOR_ICP_CREATED_NEW_DOCUMENT, dto.getIcp(), document);

            try {
                documentCrudRepository.save(document);
                log.debug(LOG_SAVED_DOCUMENT_MEDIUM_TO_DATABASE, document);
            } catch (Exception e) {
                log.warn(LOG_FAILED_TO_SAVE_DOCUMENT_TO_DATABASE, e);
            }

            DocumentDto documentDto = documentMapper.toDto(document);
            documentDto.setIcp(dto.getIcp());
            return documentDto;
        }).orElseThrow(() -> new IndividualNotFoundException(String.format(ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND,dto.getIcp())));
    }

    @Override
    public List<DocumentDto> getAllDocuments(String icp, String uuid) {
        if (uuid.equals(UUID_STRING_VALUE)) {
            return getAllDocuments(icp);
        } else {
            throw new IllegalArgumentException(ERROR_INVALID_TYPE);
        }
    }

    private void markDocumentAsNotActual(List<Document> list) {
        list.forEach(document -> {
            if (document.isActual()) {
                document.setActual(false);
            }
        });
    }

    @Override
    public DocumentDto updateDocumentActualState(DocumentDto dto) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());
        return individual.map(ind -> {
            List<Document> documents = ind.getDocuments()
                    .stream()
                    .filter(document -> document.getDocumentType().equals(dto.getDocumentType()))
                    .toList();
            markDocumentAsNotActual(documents);
            Document document = documentMapper.toEntity(dto);
            document.setUuid(UUID.randomUUID().toString());
            document.setIndividual(ind);
            document.setActual(true);
            log.info(LOG_FOR_ICP_CREATED_NEW_DOCUMENT, dto.getIcp(), document);
            documentCrudRepository.save(document);
            DocumentDto documentDto = documentMapper.toDto(document);
            documentDto.setIcp(dto.getIcp());
            return documentDto;
        }).orElseThrow(() -> new IndividualNotFoundException(String.format(ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND,dto.getIcp())));
    }
}
