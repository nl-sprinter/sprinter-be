package com.nl.sprinterbe.global.exception.user;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserException extends ApplicationException {

    public UserException(String errorCode, HttpStatus httpStatus,String message) {
        super(errorCode,httpStatus,message);
    }
}
