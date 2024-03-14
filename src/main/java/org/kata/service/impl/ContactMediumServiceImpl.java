package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.Individual;
import org.kata.entity.enums.ContactMediumType;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.ContactMediumCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.ContactMediumService;
import org.kata.service.mapper.ContactMediumMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.kata.service.impl.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactMediumServiceImpl implements ContactMediumService {

    private final ContactMediumCrudRepository contactMediumCrudRepository;
    private final IndividualCrudRepository individualCrudRepository;
    private final ContactMediumMapper contactMediumMapper;

    public List<ContactMediumDto> getContactMedium(String icp) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<ContactMedium> contacts = individual.get().getContacts();

            if (!contacts.isEmpty()) {
                List<ContactMedium> contactMedium = contacts.stream()
                        .filter(ContactMedium::isActual)
                        .collect(Collectors.toList());
                List<ContactMediumDto> contactMediumDtos = contactMediumMapper.toDto(contactMedium);
                contactMediumDtos.forEach(cm -> cm.setIcp(icp));
                return contactMediumDtos;
            } else {
                throw new ContactMediumNotFoundException(String.format(ERROR_NO_CONTACT_MEDIUM_FOUND_FOR_INDIVIDUAL, icp));
            }
        } else {
            throw new IndividualNotFoundException(String.format(ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND, icp));
        }
    }


    public ContactMediumDto saveContactMedium(ContactMediumDto dto) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());

        return individual.map(ind -> {
            List<ContactMedium> contactMediums = ind.getContacts();
            List<ContactMedium> markOldContact = contactMediums.stream()
                    .filter(contact -> dto.getType().equals(contact.getType()))
                    .collect(Collectors.toList());
            markContactMediumAsNotActual(markOldContact);

            ContactMedium contactMedium = contactMediumMapper.toEntity(dto);
            contactMedium.setUuid(UUID.randomUUID().toString());
            contactMedium.setActual(true);
            contactMedium.setIndividual(ind);

            log.info(LOG_FOR_ICP_CREATED_NEW_CONTACT_MEDIUM, dto.getIcp(), contactMedium);

            try {
                contactMediumCrudRepository.save(contactMedium);
                log.debug(LOG_SAVED_CONTACT_MEDIUM_TO_DATABASE, contactMedium);
            } catch (Exception e) {
                log.warn(LOG_FAILED_TO_SAVE_CONTACT_MEDIUM_TO_DATABASE, e);
            }

            ContactMediumDto contactMediumDto = contactMediumMapper.toDto(contactMedium);
            contactMediumDto.setIcp(dto.getIcp());
            return contactMediumDto;
        }).orElseThrow(() -> new IndividualNotFoundException(String.format(ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND, dto.getIcp())));
    }

    public ContactMedium getContactMediumByTypeAndValue(ContactMediumType type, String value) {
        return contactMediumCrudRepository
                .findByTypeAndValue(type, value)
                .orElseThrow(() -> new ContactMediumNotFoundException(
                        String.format(ERROR_NO_CONTACT_MEDIUM_FOUND_WITH_TYPE_AND_VALUE, type, value)
                ));
    }

    @Override
    public List<ContactMediumDto> getContactMedium(String icp, String uuid) {
        if (uuid.equals(UUID_STRING_VALUE)) {
            return getContactMedium(icp);
        } else {
            throw new IllegalArgumentException(ERROR_INVALID_TYPE);
        }
    }

    private void markContactMediumAsNotActual(List<ContactMedium> list) {
        list.forEach(contactMedium -> {
            if (contactMedium.isActual()) {
                contactMedium.setActual(false);
            }
        });
    }
}
