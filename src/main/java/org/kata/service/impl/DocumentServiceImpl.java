package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Document;
import org.kata.entity.Individual;
import org.kata.exception.DocumentsNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.DocumentCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.DocumentService;
import org.kata.service.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(key = "#icp", value = "icpDocuments")
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

            log.info("For icp {} created new Document: {}", dto.getIcp(), document);

            documentCrudRepository.save(document);

            Cache cacheDocuments = cacheManager.getCache("icpDocuments");
            Cache cacheIndividual = cacheManager.getCache("icpIndividual");

            if (cacheDocuments != null && cacheDocuments.get(dto.getIcp()) != null) {
                cacheDocuments.put(dto.getIcp(), dto);
            }

            if (cacheIndividual != null && cacheIndividual.get(dto.getIcp()) != null) {
                IndividualDto individualDto = (IndividualDto) cacheIndividual.get(dto.getIcp()).get();
                List<DocumentDto> documentsToUpdate = individualDto.getDocuments().stream()
                        .filter(doc -> doc.getDocumentType().equals(dto.getDocumentType()))
                        .toList();
                documentsToUpdate.forEach(doc -> doc.setActual(false));
                individualDto.getDocuments().add(dto);
                cacheIndividual.put(dto.getIcp(), individualDto);
            }

            DocumentDto documentDto = documentMapper.toDto(document);
            documentDto.setIcp(dto.getIcp());
            return documentDto;
        }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
    }

    @Override
    public List<DocumentDto> getAllDocuments(String icp, String uuid) {
        if (uuid.equals("uuid")) {
            return getAllDocuments(icp);
        } else {
            throw new IllegalArgumentException("Invalid type");
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
            log.info("For icp {} created new Document: {}", dto.getIcp(), document);
            documentCrudRepository.save(document);
            DocumentDto documentDto = documentMapper.toDto(document);
            documentDto.setIcp(dto.getIcp());
            return documentDto;
        }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
    }
}
