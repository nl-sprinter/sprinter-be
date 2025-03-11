package com.nl.sprinterbe.global.exception.userproject;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserProjectException extends ApplicationException {

    public UserProjectException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
