package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;

public class UserPasswordNotEqualsException extends UserException {

    private static final String ERROR_CODE = "406";
    private static final HttpStatus STATUS = HttpStatus.NOT_ACCEPTABLE;
    private static final String MESSAGE = "현재 비밀번호가 일치하지 않습니다.";

    public UserPasswordNotEqualsException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
