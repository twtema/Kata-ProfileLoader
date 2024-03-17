package org.kata.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.controller.dto.AccountDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.enums.CurrencyType;
import org.kata.exception.NoMoneyException;
import org.kata.service.AccountService;
import org.kata.service.CurrencyConverterService;
import org.kata.service.IndividualService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TransferringMoneyServiceImplTest {
    @Mock
    private IndividualService individualService;
    @Mock
    private AccountService accountService;
    @Mock
    private CurrencyConverterService converterService;
    @InjectMocks
    private TransferringMoneyServiceImpl transferringMoneyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Тест на проверку обновления счета у получателя и отправителя и конвертации валют
    @Test
    public void testTransferringMoneyByCardNumberSuccess() {

        IndividualDto sender = IndividualDto.builder().build();
        AccountDto accountDto = AccountDto.builder().build();
        accountDto.setBalance(new BigDecimal("500"));
        accountDto.setCurrencyType(CurrencyType.valueOf("USD"));
        sender.setAccount(List.of(accountDto));

        IndividualDto recipient = IndividualDto.builder().build();
        recipient.setAccount(List.of(accountDto));

        Mockito.when(individualService.getIndividual("111")).thenReturn(sender);
        Mockito.when(individualService.getIndividualByCardNumber("222")).thenReturn(recipient);
        Mockito.when(converterService.convertCurrency(any(), any(), any())).thenReturn(new BigDecimal("20"));

        transferringMoneyService.transferringMoneyByCardNumber("111", "222", new BigDecimal("10"));

        verify(accountService, times(2)).update(any(), any());
        verify(converterService, times(1)).convertCurrency(any(),any(), any());
    }

    //Тест на проверку выброса соответствующего исключения, если на счету отправителя не достаточно средств
    @Test
    public void testDebitsMoneyInsufficientFunds() {

        AccountDto account = AccountDto.builder().build();
        account.setBalance(new BigDecimal("100"));
        account.setCurrencyType(CurrencyType.USD);

        Assertions.assertThrows(NoMoneyException.class, () -> transferringMoneyService.debitsMoney(account, new BigDecimal("200")));
    }

    //Тест на проверку списания средств со счета отправителя
    @Test
    public void testDebitsMoney() {

        AccountDto account = AccountDto.builder().build();
        account.setAccountId("3");
        account.setBalance(new BigDecimal("200"));

        transferringMoneyService.debitsMoney(account, new BigDecimal("50"));

        verify(accountService).update("3",new BigDecimal("150"));
    }

    //Тест на проверку увеличения средств на счете получателя
    @Test
    public void testCreditsMoney() {

        AccountDto account = AccountDto.builder().build();
        account.setAccountId("1");
        account.setBalance(new BigDecimal("100"));

        transferringMoneyService.creditsMoney(account, new BigDecimal("50"));

        verify(accountService).update("1", new BigDecimal("150"));
    }
}
