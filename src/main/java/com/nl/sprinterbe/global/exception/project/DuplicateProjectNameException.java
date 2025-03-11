package com.nl.sprinterbe.global.exception.project;

import org.springframework.http.HttpStatus;

public class DuplicateProjectNameException extends ProjectException {

    private static final String ERROR_CODE = "project-409";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "project not found";

    public DuplicateProjectNameException(String message) {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
