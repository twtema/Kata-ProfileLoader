package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.exception.NoMoneyException;
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
    private final IndividualMapper individualMapper;

    @Override
    public void transferringMoneyByCardNumber(String icp, String cardNumber, BigDecimal summ) {

        //Вызвать метод Артема с отправкой кода, если true продолжать, если false выбросить соответствующее исключение

        //найти того кто отправляет
        IndividualDto sender = individualService.getIndividual(icp);
        IndividualDto recipient = individualService.getIndividualByCardNumber(cardNumber);

        if (sender != null && recipient != null) {
            debitsMoney(sender,summ);
            creditsMoney(recipient,summ);
        }

    }


    @Override
    public void debitsMoney(IndividualDto individualDto, BigDecimal summ) {
        //перевести валюту и проверить баланс
        //у индивидуала списать деньги со счета
        //сщхранить изменения в БД
        if (individualDto.getAccount().get(0).getBalance().compareTo(summ) > 0) {
            individualDto.getAccount().get(0).setBalance(individualDto.getAccount().get(0).getBalance().subtract(summ));
            individualService.saveIndividual(individualDto);
        } else {
            throw new NoMoneyException("на балансе не хватает денег для перевода");
        }
    }

    @Override
    public void creditsMoney(IndividualDto individualDto, BigDecimal summ) {
        //зачислить деньги индивидуалу на счет
        //сохранить изменения в БД
        individualDto.getAccount().get(0).setBalance(individualDto.getAccount().get(0).getBalance().add(summ));
        individualService.saveIndividual(individualDto);
    }
}
