package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.Individual;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.exception.ContactMediumUpdateException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.ContactMediumCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.ContactMediumService;
import org.kata.service.ContactMediumValueValidator;
import org.kata.service.mapper.ContactMediumMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactMediumServiceImpl implements ContactMediumService {
    private final ContactMediumCrudRepository contactMediumCrudRepository;
    private final IndividualCrudRepository individualCrudRepository;
    private final ContactMediumMapper contactMediumMapper;
    private final ContactMediumValueValidator contactMediumValueValidator;

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
                throw new ContactMediumNotFoundException("No ContactMedium found for individual with icp: " + icp);
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + icp + " not found");
        }
    }


    public ContactMediumDto saveContactMedium(ContactMediumDto dto) {
        if (contactMediumValueValidator.validateDtoContactValue(dto)) {
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

                log.info("For icp {} created new ContactMedium: {}", dto.getIcp(), contactMedium);

                contactMediumCrudRepository.save(contactMedium);

                ContactMediumDto contactMediumDto = contactMediumMapper.toDto(contactMedium);
                contactMediumDto.setIcp(dto.getIcp());
                return contactMediumDto;
            }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
        } else {
            throw new ContactMediumUpdateException(dto.getValue() + " is invalid contact value");
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
