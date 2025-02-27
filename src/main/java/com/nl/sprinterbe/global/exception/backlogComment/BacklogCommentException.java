package com.nl.sprinterbe.global.exception.backlogComment;

import com.nl.sprinterbe.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BacklogCommentException extends ApplicationException {
    public BacklogCommentException(String error, HttpStatus httpStatus, String message) {
        super(error, httpStatus,message);
    }
}
