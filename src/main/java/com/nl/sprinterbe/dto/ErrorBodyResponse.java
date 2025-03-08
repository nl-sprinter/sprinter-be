package com.nl.sprinterbe.dto;

import lombok.Getter;

@Getter
public class ErrorBodyResponse {

    private final String errorCode;
    private final String message;

    public ErrorBodyResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
