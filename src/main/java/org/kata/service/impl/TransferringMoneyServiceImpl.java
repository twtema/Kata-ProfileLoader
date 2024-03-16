package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.AccountDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.exception.NoMoneyException;
import org.kata.service.AccountService;
import org.kata.service.IndividualService;
import org.kata.service.TransferringMoneyService;
import org.kata.service.mapper.IndividualMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@Slf4j
@RequiredArgsConstructor
public class TransferringMoneyServiceImpl implements TransferringMoneyService {

    private final IndividualService individualService;
    private final AccountService accountService;

    @Override
    public void transferringMoneyByCardNumber(String icp, String cardNumber, BigDecimal summ) {
        IndividualDto sender = individualService.getIndividual(icp);
        IndividualDto recipient = individualService.getIndividualByCardNumber(cardNumber);

        if (sender != null && recipient != null) {
            debitsMoney(sender.getAccount().get(0),summ);
            creditsMoney(recipient.getAccount().get(0),summ);
        }

    }


    @Override
    public void debitsMoney(AccountDto accountDto, BigDecimal summ) {
        if (accountDto.getBalance().compareTo(summ) > 0) {
            accountDto.setBalance(accountDto.getBalance().subtract(summ));
            accountService.update(accountDto.getAccountId(),accountDto.getBalance());
        } else {
            throw new NoMoneyException("на балансе не хватает денег для перевода");
        }
    }

    @Override
    public void creditsMoney(AccountDto accountDto, BigDecimal summ) {
        accountDto.setBalance(accountDto.getBalance().add(summ));
        accountService.update(accountDto.getAccountId(), accountDto.getBalance());
    }
}
