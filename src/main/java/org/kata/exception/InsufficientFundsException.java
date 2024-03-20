package org.kata.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InsufficientFundsException extends AccountOperationException {

    private final BigDecimal balance;
    private final BigDecimal amountRequested;

    public InsufficientFundsException(BigDecimal balance, BigDecimal amountRequested) {
        super(String.format("Insufficient funds for the transaction: available balance = %s, requested amount = %s",
                balance.toPlainString(), amountRequested.toPlainString()));
        this.balance = balance;
        this.amountRequested = amountRequested;
    }
}