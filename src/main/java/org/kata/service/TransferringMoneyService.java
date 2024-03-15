package org.kata.service;

import org.kata.controller.dto.IndividualDto;

import java.math.BigDecimal;

public interface TransferringMoneyService {
    void transferringMoneyByCardNumber(String icp, String cardNumber, BigDecimal summ);

    void debitsMoney(IndividualDto individualDto, BigDecimal summ);

    void creditsMoney(IndividualDto individualDto, BigDecimal summ);
}
