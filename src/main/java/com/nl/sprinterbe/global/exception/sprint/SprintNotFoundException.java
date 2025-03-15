package com.nl.sprinterbe.global.exception.sprint;

import org.springframework.http.HttpStatus;

public class SprintNotFoundException extends SprintException{
    private static final String ERROR_CODE = "404";
    private static final String MESSAGE = "스프린트를 찾을 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public SprintNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
