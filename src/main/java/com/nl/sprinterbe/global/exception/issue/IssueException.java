package com.nl.sprinterbe.global.exception.issue;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class IssueException extends ApplicationException {

    public IssueException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
