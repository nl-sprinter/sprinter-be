package com.nl.sprinterbe.global.exception.issue;

import org.springframework.http.HttpStatus;

public class IssueNotFoundException extends IssueException {

    private static final String ERROR_CODE = "Issue-404";
    private static final String MESSAGE = "Issue를 찾을 수 없습니다.";

    public IssueNotFoundException() {
        super(ERROR_CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
