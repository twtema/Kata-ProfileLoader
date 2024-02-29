package org.kata.exception;

public class BlacklistDocumentException extends RuntimeException {
    public BlacklistDocumentException(String message) {
        super(message);
    }
}
