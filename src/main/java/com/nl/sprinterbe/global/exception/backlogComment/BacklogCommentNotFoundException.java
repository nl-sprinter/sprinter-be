package com.nl.sprinterbe.global.exception.backlogComment;

import org.springframework.http.HttpStatus;

public class BacklogCommentNotFoundException extends BacklogCommentException {
    private static final String ERROR_CODE = "BACKLOG-404";
    private static final String MESSAGE = "해당 댓글을 찾을 수 없습니다.";

    public BacklogCommentNotFoundException() {
        super(ERROR_CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
