package com.nl.sprinterbe.global.exception.project;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends ProjectException {

    private static final String ERROR_CODE = "project-404";
    private static final String MESSAGE = "Project not found";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public ProjectNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
