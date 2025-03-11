package com.nl.sprinterbe.global.exception.backlogcomment;

import org.springframework.http.HttpStatus;

public class BacklogCommentNotFoundException extends BacklogCommentException {

    private static final String ERROR_CODE = "backlogcomment-404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "BacklogComment not found.";

    public BacklogCommentNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
