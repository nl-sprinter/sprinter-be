package com.nl.sprinterbe.global.exception.issue;

import org.springframework.http.HttpStatus;

public class IssueNotFoundException extends IssueException {

    private static final String ERROR_CODE = "issue-404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "issue not found";

    public IssueNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
