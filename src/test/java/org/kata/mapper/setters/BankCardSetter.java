package org.kata.mapper.setters;

import org.kata.controller.dto.BankCardDto;
import org.kata.entity.BankCard;
import org.kata.entity.enums.BankCardType;

import java.util.Date;

public class BankCardSetter implements Setter<BankCard, BankCardDto> {
    @Override
    public void setEntityFields(BankCard bankCard) {
        bankCard.setCardNumber("2202201840607718");
        bankCard.setCvv(123);
        bankCard.setBankCardType(BankCardType.CREDIT_CARD);
        bankCard.setExpirationDate(new Date());
    }
    @Override
    public void setDtoFields(BankCardDto bankCardDto) {
        bankCardDto.setCardNumber("2202201840607718");
        bankCardDto.setBankCardType(BankCardType.CREDIT_CARD);
        bankCardDto.setCvv(123);
        bankCardDto.setExpirationDate(new Date());
    }
}
