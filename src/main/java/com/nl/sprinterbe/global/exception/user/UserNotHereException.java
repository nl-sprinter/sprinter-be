package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;

public class UserNotHereException extends UserException{
    private static final String ERROR_CODE = "400";
    private static final String MESSAGE = "탈퇴했거나 존재하지 않는 회원입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public UserNotHereException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
