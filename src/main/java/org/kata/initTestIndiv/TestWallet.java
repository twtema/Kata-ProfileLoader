package org.kata.initTestIndiv;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.Individual;
import org.kata.entity.Wallet;
import org.kata.entity.enums.CurrencyType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TestWallet extends Wallet {
    private final String uuid = UUID.randomUUID().toString();
    private final String walletId = UUID.randomUUID().toString();
    private Individual individual;
    private final CurrencyType currencyType = CurrencyType.RUB;
    private final BigDecimal balance = BigDecimal.valueOf(150000);
    private final boolean isActual = true;
}
