package com.nl.sprinterbe.global.exception.backlogComment;

import org.springframework.http.HttpStatus;

public class ForbiddenCommentAccessException extends BacklogCommentException {

    private static final String ERROR_CODE = "BACKLOG-COMMENT-403";
    private static final String MESSAGE = "댓글에 대한 권한이 없습니다.";
    public ForbiddenCommentAccessException() {
        super(ERROR_CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
