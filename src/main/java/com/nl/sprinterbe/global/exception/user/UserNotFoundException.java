package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {

    private static final String ERROR_CODE = "user-404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "유저를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
