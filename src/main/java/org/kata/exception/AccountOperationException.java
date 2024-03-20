package org.kata.exception;

public abstract class AccountOperationException extends Exception {

    public AccountOperationException(String message) {
        super(message);
    }
}