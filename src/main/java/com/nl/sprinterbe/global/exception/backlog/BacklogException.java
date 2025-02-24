package com.nl.sprinterbe.global.exception.backlog;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BacklogException extends ApplicationException {
    public BacklogException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
