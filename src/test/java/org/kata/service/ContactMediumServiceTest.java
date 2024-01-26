package org.kata.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.controller.dto.RequestContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.Individual;
import org.kata.exception.ContactMediumNotFoundException;
import org.kata.exception.ContactMediumTypeNotFoundException;
import org.kata.exception.ContactMediumUsageNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.ContactMediumCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.impl.ContactMediumServiceImpl;
import org.kata.service.mapper.ContactMediumMapper;
import org.kata.util.FileUtil;
import org.kata.util.MapperUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactMediumServiceTest {

    @Mock
    private IndividualCrudRepository individualCrudRepository;

    @Mock
    private ContactMediumCrudRepository contactMediumCrudRepository;

    @Mock
    private ContactMediumMapper contactMediumMapper;

    @InjectMocks
    private ContactMediumServiceImpl contactMediumService;

    private Individual individual;

    private IndividualDto individualDto;

    @BeforeEach
    void setUp() throws IOException {
         individual = MapperUtil.deserializeIndividual(FileUtil.readFromFileToString("TestIndividual2.json"));
         individualDto = MapperUtil.deserializeIndividualDto(FileUtil.readFromFileToString("TestIndividualDto2.json"));
    }

    @Test
    void testGetContactMediumWhenIndividualAndContactMediumExist() {
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        when(contactMediumMapper.toDto(anyList())).thenReturn(individualDto.getContacts());
        List<ContactMediumDto> resultContacts = contactMediumService.getContactMedium("GOOD_ICP");
        assertEquals(resultContacts.get(0).getType(), individual.getContacts().get(0).getType());
        assertEquals(resultContacts.get(0).getUsage(), individual.getContacts().get(0).getUsage());
        assertEquals(resultContacts.get(0).getValue(), individual.getContacts().get(0).getValue());
    }

    @Test
    void testGetContactMediumWhenIndividualNotExist() {
        assertThrows(IndividualNotFoundException.class, () -> contactMediumService.getContactMedium("BAD_ICP"));
    }

    @Test
    void testGetContactMediumWhenContactMediumNotExist() {
        individual.setContacts(new ArrayList<>());
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        assertThrows(ContactMediumNotFoundException.class, () -> contactMediumService.getContactMedium(anyString()));
    }

    @Test
    void testGetContactMediumWhenContactMediumTypeNotExist() {
        RequestContactMediumDto badTypeRequestDto = RequestContactMediumDto.builder()
                .icp("GOOD_ICP")
                .type("BAD_TYPE")
                .build();
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        when(contactMediumMapper.toDto(anyList())).thenReturn(individualDto.getContacts());
        assertThrows(ContactMediumTypeNotFoundException.class, () -> contactMediumService.getContactMedium(badTypeRequestDto));
    }

    @Test
    void testGetContactMediumWhenContactMediumUsageNotExist() {
        RequestContactMediumDto badUsageRequestDto = RequestContactMediumDto.builder()
                .icp("GOOD_ICP")
                .usage("BAD_USAGE")
                .build();
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        when(contactMediumMapper.toDto(anyList())).thenReturn(individualDto.getContacts());
        assertThrows(ContactMediumUsageNotFoundException.class, () -> contactMediumService.getContactMedium(badUsageRequestDto));

    }

    @Test
    void testSaveContactMediumWhenIndividualExist() {
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        when(contactMediumMapper.toEntity(any(ContactMediumDto.class))).thenReturn(individual.getContacts().get(0));
        when(contactMediumCrudRepository.save(any())).thenReturn(individual.getContacts().get(0));
        when(contactMediumMapper.toDto(any(ContactMedium.class))).thenReturn(individualDto.getContacts().get(0));
        assertNotNull(contactMediumService.saveContactMedium(individualDto.getContacts().get(0)));
    }

    @Test
    void testSaveContactMediumWhenIndividualNotExist() {
        ContactMediumDto badIcpDto = ContactMediumDto.builder()
                .icp("BAD_ICP")
                .build();
        assertThrows(IndividualNotFoundException.class, () -> contactMediumService.saveContactMedium(badIcpDto));
    }

}
