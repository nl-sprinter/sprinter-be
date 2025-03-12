package com.nl.sprinterbe.global.exception.task;

import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends TaskException {
    private static final String ERROR_CODE = "404";
    private static final String MESSAGE = "업무를 찾을 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public TaskNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
