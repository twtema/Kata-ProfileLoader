package org.kata.exception;

public class ContactMediumTypeNotFoundException extends RuntimeException {
    public ContactMediumTypeNotFoundException(String message) {
        super(message);
    }
}
