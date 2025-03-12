package com.nl.sprinterbe.global.exception.backlogcomment;

import org.springframework.http.HttpStatus;

public class ForbiddenCommentAccessException extends BacklogCommentException {

    private static final String ERROR_CODE = "403";
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String MESSAGE = "댓글에 접근할 수 없습니다.";

    public ForbiddenCommentAccessException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
