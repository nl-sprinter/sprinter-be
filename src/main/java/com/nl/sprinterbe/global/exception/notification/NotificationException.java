package com.nl.sprinterbe.global.exception.notification;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotificationException extends ApplicationException {
    public NotificationException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
