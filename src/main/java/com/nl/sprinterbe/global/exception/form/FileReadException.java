package com.nl.sprinterbe.global.exception.form;

import org.springframework.http.HttpStatus;

public class FileReadException extends FormException {

    private static final String ERROR_CODE = "500";
    private static final String MESSAGE = "파일을 읽는 중 오류가 발생했습니다.";
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    public FileReadException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
