package com.nl.sprinterbe.global.exception.schedule;

import org.springframework.http.HttpStatus;

public class ScheduleNotFoundException extends ScheduleException{
    private static final String ERROR_CODE = "404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "Schedule을 찾을 수 없습니다.";

    public ScheduleNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

    public ScheduleNotFoundException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
