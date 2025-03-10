package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;

public class UserPasswordNotEqualsException extends UserException {

    private static final String ERROR_CODE = "user-401";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String MESSAGE = "user password not equals";

    public UserPasswordNotEqualsException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
