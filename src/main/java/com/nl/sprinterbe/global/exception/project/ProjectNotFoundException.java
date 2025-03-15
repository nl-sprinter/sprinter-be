package com.nl.sprinterbe.global.exception.project;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends ProjectException {

    private static final String ERROR_CODE = "404";
    private static final String MESSAGE = "프로젝트를 찾을 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public ProjectNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
