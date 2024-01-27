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

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
                throw new DocumentsNotFoundException("No Document found for individual with icp: " + icp);
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + icp + " not found");
        }
    }

    public List<DocumentDto> getArchiveDocuments(String icp) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<Document> documents = individual.get().getDocuments();

            if (!documents.isEmpty()) {
                List<Document> documentList = documents.stream()
                        .filter(document -> !document.isActual())
                        .toList();
                List<DocumentDto> documentDtos = documentMapper.toDto(documentList);
                documentDtos.forEach(doc -> doc.setIcp(icp));

                return documentDtos;
            } else {
                throw new DocumentsNotFoundException("No Document found for individual with icp: " + icp);
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + icp + " not found");
        }
    }

    @Override
    public DocumentDto updateDocument(DocumentDto dto) {

        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());


        if (individual.isPresent()) {
            List<Document> documents = individual.get().getDocuments();


            List<Document> oldDoc = documents.stream()
                    .filter(doc -> dto.getDocumentType().equals(doc.getDocumentType()))
                    .toList();

            if (!oldDoc.isEmpty()) {
                Document document = documentMapper.toEntity(dto);
                for (Document doc : oldDoc) {
                    if (doc.getDocumentSerial().equals(dto.getDocumentSerial())) {
                        document.setUuid(doc.getUuid());
                        document.setActual(true);
                        document.setIndividual(individual.get());
                        document.setExternalDate(Date.from(Instant.now()));
                        document.setDocumentNumber(dto.getDocumentNumber());
                        document.setDocumentSerial(dto.getDocumentSerial());
                        document.setDocumentType(dto.getDocumentType());
                        document.setIssueDate(dto.getIssueDate());
                        document.setExpirationDate(dto.getExpirationDate());

                    }
                }
                documentCrudRepository.save(document);
                return documentMapper.toDto(document);
            } else {
                throw new DocumentsNotFoundException("No Document found for individual with icp: " + dto.getIcp());
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found");
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
            document.setExternalDate(Date.from(Instant.now()));

            log.info("For icp {} created new Document: {}", dto.getIcp(), document);

            documentCrudRepository.save(document);

            DocumentDto documentDto = documentMapper.toDto(document);
            documentDto.setIcp(dto.getIcp());
            return documentDto;
        }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
    }

    private void markDocumentAsNotActual(List<Document> list) {
        list.forEach(document -> {
            if (document.isActual()) {
                document.setActual(false);
            }
        });
    }
}
