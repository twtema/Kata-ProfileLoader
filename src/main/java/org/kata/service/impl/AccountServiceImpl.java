package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.AccountDto;
import org.kata.entity.Account;
import org.kata.entity.Individual;
import org.kata.entity.enums.ContactMediumType;
import org.kata.entity.enums.CurrencyType;
import org.kata.exception.AccountNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.AccountCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.AccountService;
import org.kata.service.ContactMediumService;
import org.kata.service.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountCrudRepository accountCrudRepository;

    private final IndividualCrudRepository individualCrudRepository;

    private final AccountMapper accountMapper;

    private final ContactMediumService contactMediumService;


    @Override
    public List<AccountDto> getAccount(String icp) {
        List<Account> accounts = getIndividual(icp).getAccount();
        if (!accounts.isEmpty()) {
            return accounts
                    .stream()
                    .filter(Account::isActual)
                    .map(account -> {
                        AccountDto accountDto = accountMapper.toDto(account);
                        accountDto.setIcp(icp);
                        return accountDto;
                    })
                    .toList();
        }
        throw new AccountNotFoundException(String.format("No wallets for icp %s found", icp));
    }

    @Override
    public AccountDto saveAccount(AccountDto dto) {
        Individual ind = getIndividual(dto.getIcp());
        List<Account> accounts = ind.getAccount();
        List<Account> oldAccount = accounts.stream()
                .filter(account -> dto.getCurrencyType().equals(account.getCurrencyType()) && account.isActual())
                .toList();
        oldAccount.forEach(wal -> {
            dto.setBalance(dto.getBalance().add(wal.getBalance()));
            wal.setBalance(BigDecimal.ZERO);
        });
        markAccountAsNotActual(oldAccount);

        Account account = accountMapper.toEntity(dto);
        account.setUuid(UUID.randomUUID().toString());
        account.setActual(true);
        account.setIndividual(ind);

        log.info("For icp {} created new Account: {}", dto.getIcp(), account);

        try {
            accountCrudRepository.save(account);
            log.debug("Saved Account to the database: {}", account);
        } catch (Exception e) {
            log.warn("Failed to save Account to the database.", e);
        }

        AccountDto accountDto = accountMapper.toDto(account);
        accountDto.setIcp(dto.getIcp());
        return accountDto;
    }

    private void markAccountAsNotActual(List<Account> list) {
        list.forEach(account -> {
            if (account.isActual()) {
                account.setActual(false);
            }
        });
    }

    private Individual getIndividual(String icp) {
        return individualCrudRepository
                .findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException(String.format("Individual with icp %s not found", icp)));
    }

    private Account getAccountByAccountId(String accountId) {
        return accountCrudRepository
                .findByAccountId(accountId)
                .filter(Account::isActual)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Wallet with id %s not found", accountId)));
    }

    public AccountDto getAccountByMobileAndCurrency(String mobile, CurrencyType currencyType) {

        return accountMapper.toDto(contactMediumService
                .getContactMediumByTypeAndValue(ContactMediumType.PHONE, mobile)
                .getIndividual()
                .getAccount()
                .stream()
                .filter(account -> account.getCurrencyType().compareTo(currencyType) == 0 && account.isActual())
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Wallet for mobile %s with currency %s not found", mobile, currencyType)
                )));
    }

    @Override
    public AccountDto update(String accountId, BigDecimal balance) {
        Account account = accountCrudRepository.findByAccountId(accountId).orElseThrow(
                () -> new AccountNotFoundException(String.format("Account with accountId %s not found", accountId))
        );
        account.setBalance(balance);
        return accountMapper.toDto(accountCrudRepository.save(account));
    }
}