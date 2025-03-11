package com.nl.sprinterbe.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 중요: 401 에러는 사용하지 말 것!!! 클라이언트 axios 에서 걸러짐
     */

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, String>> handleApplicationException(ApplicationException applicationException) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", applicationException.getErrorCode());
        errorResponse.put("message", applicationException.getMessage());
        return ResponseEntity.status(applicationException.getHttpStatus()).body(errorResponse);
    }
}
