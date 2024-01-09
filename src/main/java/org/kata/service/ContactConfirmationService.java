package org.kata.service;

import org.kata.controller.dto.ContactChangeMessageDTO;
import org.kata.entity.ContactChangeMessage;

import java.util.List;
import java.util.Optional;

public interface ContactConfirmationService {

    Optional<ContactChangeMessage> getConfirmationCode(String icp);

    List<ContactChangeMessage> findAll();

    ContactChangeMessage save(ContactChangeMessageDTO dto);
}
