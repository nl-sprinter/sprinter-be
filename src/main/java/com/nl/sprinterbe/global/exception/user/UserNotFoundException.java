package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {

    private static final String ERROR_CODE = "user-404";
    private static final String MESSAGE = "사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(ERROR_CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
