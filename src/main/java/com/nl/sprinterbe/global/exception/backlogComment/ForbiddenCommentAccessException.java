package com.nl.sprinterbe.global.exception.backlogComment;

import org.springframework.http.HttpStatus;

public class ForbiddenCommentAccessException extends BacklogCommentException {

    private static final String ERROR_CODE = "backlogcomment-403";
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String MESSAGE = "Backlogcomment forbidden";

    public ForbiddenCommentAccessException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
