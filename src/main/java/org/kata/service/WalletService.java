package org.kata.service;

import org.kata.controller.dto.WalletDto;
import org.kata.entity.enums.CurrencyType;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    List<WalletDto> getWallet(String icp);

    WalletDto saveWallet(WalletDto dto);

    WalletDto getWalletByMobileAndCurrency(String mobile, CurrencyType currencyType);

    WalletDto update(String walletId, BigDecimal balance);

}
