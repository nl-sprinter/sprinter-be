package com.nl.sprinterbe.global.exception.issue;

import org.springframework.http.HttpStatus;

public class IssueNotFoundException extends IssueException {

    private static final String ERROR_CODE = "404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "이슈를 찾을 수 없습니다.";

    public IssueNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
