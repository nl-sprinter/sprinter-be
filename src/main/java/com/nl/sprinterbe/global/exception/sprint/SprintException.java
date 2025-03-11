package com.nl.sprinterbe.global.exception.sprint;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class SprintException extends ApplicationException {

    public SprintException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
