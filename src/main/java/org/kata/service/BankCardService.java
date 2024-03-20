package org.kata.service;

import org.kata.controller.dto.BankCardDto;

import java.util.List;

public interface BankCardService {
    List<BankCardDto> getAllBankCards(String icp);

    BankCardDto saveBankCard(BankCardDto dto);

    List<BankCardDto> getAllBankCards(String icp, String uuid);

    void deleteBankCard(String icp);

}
