package org.kata.exception;

public class ContactMediumUsageNotFoundException extends RuntimeException {
    public ContactMediumUsageNotFoundException(String message) {
        super(message);
    }
}
