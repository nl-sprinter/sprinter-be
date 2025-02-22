package com.nl.sprinterbe.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public abstract class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public ApplicationException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
