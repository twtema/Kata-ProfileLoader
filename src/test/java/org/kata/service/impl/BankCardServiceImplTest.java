package org.kata.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.kata.controller.dto.BankCardDto;
import org.kata.entity.BankCard;
import org.kata.entity.Individual;
import org.kata.repository.BankCardCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.mapper.BankCardMapper;
import org.kata.util.FileUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kata.util.MapperUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class BankCardServiceImplTest {
    @Mock
    private BankCardCrudRepository bankCardCrudRepository;
    @Mock
    private IndividualCrudRepository individualCrudRepository;


    @Mock
    private BankCardMapper bankCardMapper;
    @InjectMocks
    private BankCardServiceImpl bankCardService;

    private BankCardDto testBankCardDto;
    private BankCard testBankCard;

    @Before
    public void setUp() throws IOException {
        String BankCardDtoJson = FileUtil.readFromFileToString("TestBankCardDto.json");
        testBankCardDto = deserializeBankCardDto(BankCardDtoJson);
        String BankCardJson = FileUtil.readFromFileToString("TestBankCard.json");
        testBankCard = deserializeBankCard(BankCardJson);

    }

    @Test
    public void test_saveBankCard() {
        when(bankCardMapper.toEntity(Mockito.any(BankCardDto.class))).thenReturn(testBankCard);
        when(bankCardMapper.toDto(any(BankCard.class))).thenReturn(testBankCardDto);
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(new Individual()));
        when(bankCardCrudRepository.save(any(BankCard.class))).thenReturn(testBankCard);

        assertNotNull(bankCardService.saveBankCard(testBankCardDto));

        Mockito.verify(bankCardCrudRepository, Mockito.times(1)).save(any(BankCard.class));
    }

    @Test
    public void test_bankCardExists() {
        BankCard testBankCard = Mockito.mock(BankCard.class);

        when(testBankCard.getCardNumber()).thenReturn("2202201840607718");
        when(testBankCard.getCvv()).thenReturn(111);
        Individual individual = new Individual();
        individual.setBankCard(new ArrayList<>());
        individual.getBankCard().add(testBankCard);

        assertTrue(bankCardService.bankCardExists(individual, testBankCardDto.getCardNumber(), testBankCardDto.getCvv()));
    }
}
