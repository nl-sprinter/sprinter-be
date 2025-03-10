package com.nl.sprinterbe.global.exception;

import com.nl.sprinterbe.dto.ErrorBodyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorBodyResponse> handleApplicationException(ApplicationException e) {

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorBodyResponse(e.getErrorCode(), e.getMessage()));
    }
}
