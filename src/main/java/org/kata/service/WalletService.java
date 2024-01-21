package org.kata.service;
import org.kata.controller.dto.WalletDto;
import java.util.List;
import java.math.BigDecimal;
import org.kata.entity.enums.CurrencyType;

public interface WalletService {

    List<WalletDto> getWallet(String icp);

    WalletDto saveWallet(WalletDto dto);

    WalletDto getWalletByMobileAndCurrency(String mobile, CurrencyType currencyType);

    WalletDto update(String walletId, BigDecimal balance);

}
