package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.ContactChangeMessageDTO;
import org.kata.entity.ContactChangeMessage;
import org.kata.repository.ContactChangeMessageCrudRepository;
import org.kata.service.ContactConfirmationService;
import org.kata.service.mapper.ContactChangeMessageMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactConfirmationServiceImpl implements ContactConfirmationService {

    private final ContactChangeMessageCrudRepository contactChangeMessageCrudRepository;
    private final ContactChangeMessageMapper contactChangeMessageMapper;

    @Cacheable
    public List<ContactChangeMessage> findAll() {
        return contactChangeMessageCrudRepository.findAll();
    }

    @Cacheable(key = "#icp")
    public Optional<ContactChangeMessage> getConfirmationCode(String icp) {
        return contactChangeMessageCrudRepository.findById(icp);
    }

    @CachePut(key = "dto.icp")
    public ContactChangeMessage save(ContactChangeMessageDTO dto) {
        return contactChangeMessageCrudRepository.save(contactChangeMessageMapper.toEntity(dto));
    }
}
