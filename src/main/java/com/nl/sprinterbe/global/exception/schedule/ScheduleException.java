package com.nl.sprinterbe.global.exception.schedule;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ScheduleException extends ApplicationException {
    public ScheduleException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
