package com.nl.sprinterbe.global.exception.project;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends ProjectException {

    private static final String errorCode = "project-404";
    private static final String message = "Project not found";

    public ProjectNotFoundException() {
        super(errorCode, HttpStatus.NOT_FOUND, message);
    }
}
