package org.kata.exception;

public class NoMoneyException  extends RuntimeException{
    public NoMoneyException(String message) {
        super(message);
    }
}
