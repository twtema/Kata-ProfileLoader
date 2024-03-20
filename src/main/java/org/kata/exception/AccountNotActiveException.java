package org.kata.exception;

import lombok.Getter;

@Getter
public class AccountNotActiveException extends AccountOperationException {

    private final String accountId;

    public AccountNotActiveException(String accountId) {
        super(String.format("Account with ID %s is not active", accountId));
        this.accountId = accountId;
    }
}
