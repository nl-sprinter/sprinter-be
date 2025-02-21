package com.nl.sprinterbe.global.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
