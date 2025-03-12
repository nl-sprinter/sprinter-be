package com.nl.sprinterbe.global.exception.backlogcomment;

import org.springframework.http.HttpStatus;

public class BacklogCommentNotFoundException extends BacklogCommentException {

    private static final String ERROR_CODE = "404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "댓글을 찾을 수 없습니다.";

    public BacklogCommentNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
