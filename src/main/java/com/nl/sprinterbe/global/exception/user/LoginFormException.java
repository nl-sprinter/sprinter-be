package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;


public class LoginFormException extends UserException {

    private static final String ERROR_CODE = "400";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "로그인 형식이 맞지 않습니다.";

    public LoginFormException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
