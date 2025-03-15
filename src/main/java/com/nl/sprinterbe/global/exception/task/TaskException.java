package com.nl.sprinterbe.global.exception.task;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TaskException extends ApplicationException {
    public TaskException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode,httpStatus,message);
    }
}
