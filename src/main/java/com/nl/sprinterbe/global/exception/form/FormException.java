package com.nl.sprinterbe.global.exception.form;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FormException extends ApplicationException {
    public FormException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode,httpStatus,message);
    }
}
