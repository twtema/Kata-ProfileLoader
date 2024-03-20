package org.kata.exception;

import lombok.Getter;

@Getter
public class AccountInCacheNotFoundException extends AccountOperationException {

    private final String uuid;
    public AccountInCacheNotFoundException(String uuid) {
        super(String.format("Account with UUID %s not found in cache", uuid));
        this.uuid = uuid;
    }
}
