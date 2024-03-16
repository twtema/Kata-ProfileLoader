package org.kata.mapper.setters;


import org.kata.controller.dto.AccountDto;
import org.kata.entity.Account;
import org.kata.entity.enums.CurrencyType;

import java.math.BigDecimal;


public class WalletSetter implements Setter<Account, AccountDto> {

    @Override
    public void setEntityFields(Account account) {
        StringFieldsSetterUtil.set(account);
        account.setBalance(BigDecimal.valueOf(33.33333));
        account.setCurrencyType(CurrencyType.BYN);
    }

    @Override
    public void setDtoFields(AccountDto accountDto) {
        StringFieldsSetterUtil.set(accountDto);
        accountDto.setCurrencyType(CurrencyType.RUB);
        accountDto.setBalance(BigDecimal.valueOf(66.66666));
    }
}
