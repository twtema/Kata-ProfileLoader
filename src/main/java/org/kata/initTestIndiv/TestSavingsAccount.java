package org.kata.initTestIndiv;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.Individual;
import org.kata.entity.SavingsAccount;
import org.kata.entity.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TestSavingsAccount extends SavingsAccount {
    private final String uuid = UUID.randomUUID().toString();
    private final Currency currency = Currency.RUB;
    private final String infoOfPercent = "noInfo";
    private final BigDecimal finalSum = BigDecimal.valueOf(200000);
    private final Short depositTerm = 12;
    private Individual individual;
}
