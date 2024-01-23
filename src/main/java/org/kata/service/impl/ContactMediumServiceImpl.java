package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.RequestContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.Individual;
import org.kata.entity.enums.ContactMediumType;
import org.kata.entity.enums.ContactMediumUsageType;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.exception.ContactMediumTypeNotFoundException;
import org.kata.exception.ContactMediumUsageNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.ContactMediumCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.ContactMediumService;
import org.kata.service.mapper.ContactMediumMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());

        return individual.map(ind -> {
            List<ContactMedium> contactMediums = ind.getContacts();
            List<ContactMedium> markOldContact = contactMediums.stream()
                    .filter(contact -> dto.getType().equals(contact.getType()))
                    .filter(contact -> dto.getUsage().equals(contact.getUsage()))
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
    }

    @Override
    public List<ContactMediumDto> getContactMedium(RequestContactMediumDto dto) {
        List<ContactMediumDto> contacts = getContactMedium(dto.getIcp());
        if (dto.getType() != null) {
            contacts = filterByType(contacts, dto.getType());
        }
        if (dto.getUsage() != null) {
            contacts = filterByUsage(contacts, dto.getUsage());
        }
        if (contacts.isEmpty()) {
            throw new ContactMediumNotFoundException(
                    "No ContactMedium with parameters found for individual with icp: " + dto.getIcp());
        }
        return contacts;
    }

    private void markContactMediumAsNotActual(List<ContactMedium> list) {
        list.forEach(contactMedium -> {
            if (contactMedium.isActual()) {
                contactMedium.setActual(false);
            }
        });
    }

    private List<String> getAllTypes() {
        return Arrays.stream(ContactMediumType.values())
                .map(Enum::toString)
                .toList();
    }

    private List<String> getAllUsages() {
        return Arrays.stream(ContactMediumUsageType.values())
                .map(Enum::toString)
                .toList();
    }

    private List<ContactMediumDto> filterByType(List<ContactMediumDto> contacts, String type) {
        if (getAllTypes().contains(type)) {
            return contacts.stream()
                    .filter(contact -> type.equals(contact.getType().toString()))
                    .toList();
        } else {
            throw new ContactMediumTypeNotFoundException("Invalid type: " + type);
        }
    }

    private List<ContactMediumDto> filterByUsage(List<ContactMediumDto> contacts, String usage) {
        if (getAllUsages().contains(usage)) {
            return contacts.stream()
                    .filter(contact -> usage.equals(contact.getUsage().toString()))
                    .toList();
        } else {
            throw new ContactMediumUsageNotFoundException("Invalid usage: " + usage);
        }
    }
}
