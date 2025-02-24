package com.nl.sprinterbe.global.exception.backlog;

import org.springframework.http.HttpStatus;

public class BacklogNotFoundException extends BacklogException {

    private static final String ERROR_CODE = "Backlog-404";
    private static final String MESSAGE = "Backlog을 찾을 수 없습니다.";

    public BacklogNotFoundException() {
        super(ERROR_CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
