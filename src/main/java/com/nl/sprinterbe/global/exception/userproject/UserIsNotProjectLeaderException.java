package com.nl.sprinterbe.global.exception.userproject;

import org.springframework.http.HttpStatus;

public class UserIsNotProjectLeaderException extends UserProjectException {

    private static final String ERROR_CODE = "406";
    private static final HttpStatus STATUS = HttpStatus.NOT_ACCEPTABLE;
    private static final String MESSAGE = "유저가 팀원이 아닙니다.";

    public UserIsNotProjectLeaderException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
