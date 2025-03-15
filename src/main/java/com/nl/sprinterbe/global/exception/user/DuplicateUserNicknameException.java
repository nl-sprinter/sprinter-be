package com.nl.sprinterbe.global.exception.user;

import org.springframework.http.HttpStatus;

public class DuplicateUserNicknameException extends UserException {

    private static final String ERROR_CODE = "409";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "중복된 닉네임이 있습니다.";

    public DuplicateUserNicknameException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
