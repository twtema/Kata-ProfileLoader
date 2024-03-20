package org.kata.exception;

import lombok.Getter;
import org.kata.entity.enums.CurrencyType;

@Getter
public class AccountWrongCurrencyTypeException extends AccountOperationException {

    private final String accountId;
    private final CurrencyType currencyType;

    public AccountWrongCurrencyTypeException(String accountId, CurrencyType currencyType) {
        super(String.format(
                "Account with ID %s does not support operations with currency type %s.",
                accountId,
                currencyType
        ));
        this.accountId = accountId;
        this.currencyType = currencyType;
    }
}
