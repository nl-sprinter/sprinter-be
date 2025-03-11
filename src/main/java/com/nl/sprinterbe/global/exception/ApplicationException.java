package com.nl.sprinterbe.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public ApplicationException( String errorCode,HttpStatus httpStatus,String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

}