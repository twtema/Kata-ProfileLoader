package org.kata.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.kata.controller.dto.AccountDto;
import org.kata.controller.dto.AccountOperationDto;
import org.kata.entity.Account;
import org.kata.entity.enums.CurrencyType;
import org.kata.entity.enums.OperationType;
import org.kata.exception.AccountNotActiveException;
import org.kata.exception.AccountOperationException;
import org.kata.exception.AccountWrongCurrencyTypeException;
import org.kata.exception.InsufficientFundsException;
import org.kata.repository.AccountCrudRepository;
import org.kata.service.mapper.AccountMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.kata.entity.enums.CurrencyType.*;
import static org.kata.entity.enums.CurrencyType.CNY;
import static org.kata.entity.enums.CurrencyType.RUB;
import static org.kata.entity.enums.OperationType.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class AccountServiceImplTest {

    @Mock
    private AccountCrudRepository accountCrudRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @BeforeEach
    void setUp() {
        when(cacheManager.getCache("accountOperation")).thenReturn(cache);
    }

    @Test
    void testPerformAccountOperationDeposit_Success() {

        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId("account-id")
                .amount(new BigDecimal("100.00"))
                .operationType(DEPOSIT)
                .currencyType(RUB)
                .build();

        Account account = new Account();
        account.setActual(true);
        account.setBalance(new BigDecimal("300.00"));
        account.setCurrencyType(RUB);
        account.setUuid("account-id");

        AccountDto expectedAccountDto = AccountDto.builder()
                .balance(new BigDecimal("400.00"))
                .currencyType(RUB)
                .accountId("account-id")
                .icp("test-icp")
                .build();

        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId("account-id")).thenReturn(Optional.of(account));
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedAccountDto);

        AccountDto resultDto = null;
        try {
            resultDto = accountService.performAccountOperation(anyString());
        } catch (AccountOperationException e) {
            fail("Исключение не ожидалось", e);
        }

        assertNotNull(resultDto, "AccountDto не должен быть null");
        assertEquals(expectedAccountDto.getBalance(), resultDto.getBalance(), "Баланс должен быть равен 200");
    }

    @Test
    void testPerformAccountOperationDeposit_throwAccountNotActiveException() {
        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId("account-id")
                .amount(new BigDecimal("100.00"))
                .operationType(DEPOSIT)
                .currencyType(RUB)
                .build();

        Account account = new Account();
        account.setActual(false);
        account.setBalance(new BigDecimal("300.00"));
        account.setCurrencyType(RUB);
        account.setUuid("account-id");

        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId("account-id")).thenReturn(Optional.of(account));

        assertThrows(AccountNotActiveException.class,
                () -> accountService.performAccountOperation(anyString()),
                "Должно быть выброшено исключение AccountNotActiveException");
    }

    @Test
    void performAccountWithDraw_Success() {

        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId("account-id")
                .amount(new BigDecimal("100.00"))
                .operationType(WITHDRAW)
                .currencyType(RUB)
                .build();

        Account account = new Account();
        account.setActual(true);
        account.setBalance(new BigDecimal("300.00"));
        account.setCurrencyType(RUB);
        account.setUuid("account-id");

        AccountDto expectedAccountDto = AccountDto.builder()
                .balance(new BigDecimal("200.00"))
                .currencyType(RUB)
                .accountId("account-id")
                .icp("test-icp")
                .build();

        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId("account-id")).thenReturn(Optional.of(account));
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedAccountDto);

        AccountDto resultDto = null;
        try {
            resultDto = accountService.performAccountOperation(anyString());
        } catch (AccountOperationException e) {
            fail("Исключение не ожидалось", e);
        }

        assertNotNull(resultDto, "AccountDto не должен быть null");
        assertEquals(expectedAccountDto.getBalance(), resultDto.getBalance(), "Баланс должен быть равен 200");
    }

    @Test
    void performAccountWithDraw_throwInsufficientFundsException() {

        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId("account-id")
                .amount(new BigDecimal("1000.00"))
                .operationType(WITHDRAW)
                .currencyType(RUB)
                .build();

        Account account = new Account();
        account.setActual(true);
        account.setBalance(new BigDecimal("300.00"));
        account.setCurrencyType(RUB);
        account.setUuid("account-id");

        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId("account-id")).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class,
                () -> accountService.performAccountOperation(anyString()),
                "Должно быть выброшено исключение InsufficientFundsException");
    }

    @Test
    void testPerformAccountOperationDeposit_throwAccountWrongCurrencyTypeException() {

        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId("account-id")
                .amount(new BigDecimal("100.00"))
                .operationType(DEPOSIT)
                .currencyType(CNY)
                .build();

        Account account = new Account();
        account.setActual(true);
        account.setBalance(new BigDecimal("300.00"));
        account.setCurrencyType(RUB);
        account.setUuid("account-id");

        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId("account-id")).thenReturn(Optional.of(account));

        assertThrows(AccountWrongCurrencyTypeException.class,
                () -> accountService.performAccountOperation(anyString()),
                "Должно быть выброшено исключение AccountWrongCurrencyTypeException");
    }

    @Test
    void testPerformAccountOperationTransfer_Success() {

        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId("withdraw-account-id")
                .destinationAccountId("destination-account-id")
                .amount(new BigDecimal("100.00"))
                .operationType(TRANSFER)
                .currencyType(RUB)
                .build();

        Account withdrawAccount = new Account();
        withdrawAccount.setActual(true);
        withdrawAccount.setBalance(new BigDecimal("300.00"));
        withdrawAccount.setCurrencyType(RUB);
        withdrawAccount.setUuid("withdraw-account-id");

        AccountDto expectedAccountDto = AccountDto.builder()
                .balance(new BigDecimal("200.00"))
                .currencyType(RUB)
                .accountId("withdraw-account-id")
                .icp("test-icp")
                .build();

        when(cache.get(anyString())).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId("withdraw-account-id")).thenReturn(Optional.of(withdrawAccount));
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedAccountDto);

        AccountDto resultDto = null;
        try {
            resultDto = accountService.performAccountOperation(anyString());
        } catch (AccountOperationException e) {
            fail("Исключение не ожидалось", e);
        }

        assertNotNull(resultDto, "AccountDto не должен быть null");
        assertEquals(expectedAccountDto.getBalance(), resultDto.getBalance(), "Баланс должен быть равен 200");
    }

    @Test
    void testPerformAccountOperationLock_Success() {

        String accountId = "account-id";
        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId(accountId)
                .operationType(LOCKING)
                .build();

        Account account = new Account();
        account.setActual(true);
        account.setBalance(new BigDecimal("1000.00"));
        account.setCurrencyType(RUB);
        account.setUuid(accountId);

        AccountDto expectedAccountDto = AccountDto.builder()
                .accountId(accountId)
                .balance(new BigDecimal("1000.00"))
                .currencyType(RUB)
                .build();

        when(cache.get(accountId)).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedAccountDto);

        try {
            accountService.performAccountOperation(accountId);
        } catch (AccountOperationException e) {
            fail("Исключение не ожидалось", e);
        }

        verify(accountCrudRepository).save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();
        assertFalse(savedAccount.isActual(), "Аккаунт должен быть заблокирован");

        verify(accountMapper).toDto(savedAccount);
    }

    @Test
    void testPerformAccountOperationUnlock_Success() {

        String accountId = "account-id";
        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .accountId(accountId)
                .operationType(UNLOCKING)
                .build();

        Account account = new Account();
        account.setActual(false);
        account.setBalance(new BigDecimal("1000.00"));
        account.setCurrencyType(RUB);
        account.setUuid(accountId);

        AccountDto expectedAccountDto = AccountDto.builder()
                .accountId(accountId)
                .balance(new BigDecimal("1000.00"))
                .currencyType(RUB)
                .build();

        when(cache.get(accountId)).thenReturn(new SimpleValueWrapper(accountOperationDto));
        when(accountCrudRepository.findByAccountId(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedAccountDto);

        try {
            accountService.performAccountOperation(accountId);
        } catch (AccountOperationException e) {
            fail("Исключение не ожидалось", e);
        }

        verify(accountCrudRepository).save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();
        assertTrue(savedAccount.isActual(), "Аккаунт должен быть разблокирован");

        verify(accountMapper).toDto(savedAccount);
    }
}