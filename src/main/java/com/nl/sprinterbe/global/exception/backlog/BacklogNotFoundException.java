package com.nl.sprinterbe.global.exception.backlog;

import org.springframework.http.HttpStatus;

public class BacklogNotFoundException extends BacklogException {

    private static final String ERROR_CODE = "404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "백로그를 찾을 수 없습니다.";

    public BacklogNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
