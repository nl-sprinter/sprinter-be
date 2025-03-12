package com.nl.sprinterbe.global.exception.project;

import org.springframework.http.HttpStatus;

public class DuplicateProjectNameException extends ProjectException {

    private static final String ERROR_CODE = "409";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "프로젝트 이름이 중복 됐습니다.";

    public DuplicateProjectNameException(String message) {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
