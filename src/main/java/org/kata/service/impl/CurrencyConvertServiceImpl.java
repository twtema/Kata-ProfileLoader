package org.kata.service.impl;

import org.kata.entity.enums.CurrencyType;
import org.kata.service.CurrencyConverterService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class CurrencyConvertServiceImpl implements CurrencyConverterService {
    @Override
    public BigDecimal convertCurrency(BigDecimal summ, CurrencyType fromCurrency, CurrencyType toCurrency) {
        BigDecimal result = BigDecimal.ZERO;

        switch (fromCurrency) {
            case RUB:
                switch (toCurrency) {
                    case RUB:
                        result = summ;
                        break;
                    case BYN:
                        result = summ.multiply(new BigDecimal("0.3"));
                        break;
                    case USD:
                        result = summ.multiply(new BigDecimal("0.14"));
                        break;
                    case CNY:
                        result = summ.multiply(new BigDecimal("0.091"));
                        break;
                }
                break;
            case BYN:
                switch (toCurrency) {
                    case RUB:
                        result = summ.multiply(new BigDecimal("33.4"));
                        break;
                    case BYN:
                        result = summ;
                        break;
                    case USD:
                        result = summ.multiply(new BigDecimal("0.47"));
                        break;
                    case CNY:
                        result = summ.multiply(new BigDecimal("3.05"));
                        break;
                }
                break;
            case USD:
                switch (toCurrency) {
                    case RUB:
                        result = summ.multiply(new BigDecimal("70.9"));
                        break;
                    case BYN:
                        result = summ.multiply(new BigDecimal("2.12"));
                        break;
                    case USD:
                        result = summ;
                        break;
                    case CNY:
                        result = summ.multiply(new BigDecimal("6.44"));
                        break;
                }
                break;
            case CNY:
                switch (toCurrency) {
                    case RUB:
                        result = summ.multiply(new BigDecimal("11.0"));
                        break;
                    case BYN:
                        result = summ.multiply(new BigDecimal("0.33"));
                        break;
                    case USD:
                        result = summ.multiply(new BigDecimal("0.16"));
                        break;
                    case CNY:
                        result = summ;
                        break;
                }
                break;
        }
        return result;
    }
}
