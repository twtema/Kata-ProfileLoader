package org.kata.service;

import org.kata.entity.enums.CurrencyType;

import java.math.BigDecimal;

public interface CurrencyConverterService {
    BigDecimal convertCurrency(BigDecimal summ, CurrencyType fromCurrency, CurrencyType toCurrency);
}
