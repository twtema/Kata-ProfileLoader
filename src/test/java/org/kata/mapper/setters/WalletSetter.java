package org.kata.mapper.setters;


import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.WalletDto;
import org.kata.entity.Document;
import org.kata.entity.Wallet;
import org.kata.entity.enums.CurrencyType;
import org.kata.entity.enums.DocumentType;

import java.math.BigDecimal;
import java.util.Date;


public class WalletSetter implements Setter<Wallet, WalletDto> {

    @Override
    public void setEntityFields(Wallet wallet) {
        StringFieldsSetterUtil.set(wallet);
        wallet.setBalance(BigDecimal.valueOf(33.33333));
        wallet.setCurrencyType(CurrencyType.BYN);
    }

    @Override
    public void setDtoFields(WalletDto walletDto) {
        StringFieldsSetterUtil.set(walletDto);
        walletDto.setCurrencyType(CurrencyType.RUB);
        walletDto.setBalance(BigDecimal.valueOf(66.66666));
    }
}
