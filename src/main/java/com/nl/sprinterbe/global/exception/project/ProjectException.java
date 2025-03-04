package com.nl.sprinterbe.global.exception.project;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ProjectException extends ApplicationException {
    public ProjectException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode,httpStatus,message);

    }
}
