package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;
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


        //найти поле баланс у отправителя и отминусовать summ если там хватает денег, и сохранить изменения в БД
        sender.getWallet().get(0).getCurrencyType(); // если счет в валюте но перевести в рубли по курсу

        BigDecimal currentBalance = sender.getWallet().get(0).getBalance();
        sender.getWallet().get(0).setBalance(currentBalance.subtract(summ));
        if (currentBalance.compareTo(summ) > 0) {
            // иначе выбросить  соответствующее исключение
            throw new NoMoneyException("на балансе не хватает денег для перевода");
        }


    }


    @Override
    public void debitsMoney(IndividualDto individualDto, BigDecimal summ) {
        //перевести валюту и проверить баланс
        //у индивидуала списать деньги со счета
        //сщхранить изменения в БД
    }

    @Override
    public void creditsMoney(IndividualDto individualDto, BigDecimal summ) {
        //зачислить деньги индивидуалу на счет
        //сохранить изменения в БД
    }
}
