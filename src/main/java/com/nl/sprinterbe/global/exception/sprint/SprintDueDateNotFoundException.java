package com.nl.sprinterbe.global.exception.sprint;

import org.springframework.http.HttpStatus;

public class SprintDueDateNotFoundException extends SprintException {

    private static final String ERROR_CODE = "issue-404";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "sprint due date(startDate or endDate) not found";

    public SprintDueDateNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
