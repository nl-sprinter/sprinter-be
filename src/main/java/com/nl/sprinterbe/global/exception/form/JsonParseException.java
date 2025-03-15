package com.nl.sprinterbe.global.exception.form;

import org.springframework.http.HttpStatus;

public class JsonParseException extends FormException {

    private static final String ERROR_CODE = "500";
    private static final String MESSAGE = "JSON 파싱 중 오류가 발생했습니다.";
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public JsonParseException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
