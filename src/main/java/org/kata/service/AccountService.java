package org.kata.service;
import org.kata.controller.dto.AccountDto;

import java.util.List;
import java.math.BigDecimal;
import org.kata.entity.enums.CurrencyType;

public interface AccountService {

    List<AccountDto> getAccount(String icp);

    AccountDto saveAccount(AccountDto dto);

    AccountDto getAccountByMobileAndCurrency(String mobile, CurrencyType currencyType);

    AccountDto update(String accountId, BigDecimal balance);
}
