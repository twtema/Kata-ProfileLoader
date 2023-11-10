package org.kata.mapper.setters;


import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.WalletDto;
import org.kata.entity.Document;
import org.kata.entity.Wallet;
import org.kata.entity.enums.CurrencyType;
import org.kata.entity.enums.DocumentType;

import java.math.BigDecimal;
import java.util.Date;


public class WalletSetter implements Setter {

    @Override
    public void setEntityFields(Object walletObject) {
        Wallet wallet = (Wallet) walletObject;
        StringFieldsSetterUtil.set(walletObject);
        wallet.setBalance(BigDecimal.valueOf(33.33333));
        wallet.setCurrencyType(CurrencyType.BYN);
    }

    @Override
    public void setDtoFields(Object walletDtoObject) {
        WalletDto walletDto = (WalletDto) walletDtoObject;
        StringFieldsSetterUtil.set(walletDtoObject);
        walletDto.setCurrencyType(CurrencyType.RUB);
        walletDto.setBalance(BigDecimal.valueOf(66.66666));
    }
}
