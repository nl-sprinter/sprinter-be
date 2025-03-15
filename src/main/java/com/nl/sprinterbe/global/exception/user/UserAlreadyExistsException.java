package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends UserException {
    private static final String ERROR_CODE = "406";
    private static final HttpStatus STATUS = HttpStatus.NOT_ACCEPTABLE;
    private static final String MESSAGE = "해당 유저는 이미 존재합니다.";

    public UserAlreadyExistsException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
