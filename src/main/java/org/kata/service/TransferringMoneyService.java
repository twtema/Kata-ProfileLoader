package org.kata.service;

import org.kata.controller.dto.AccountDto;
import org.kata.controller.dto.IndividualDto;

import java.math.BigDecimal;

public interface TransferringMoneyService {
    void transferringMoneyByCardNumber(String icp, String cardNumber, BigDecimal summ);

    void debitsMoney(AccountDto accountDto, BigDecimal summ);

    void creditsMoney(AccountDto accountDto, BigDecimal summ);
}
