package com.nl.sprinterbe.global.exception;

import javax.naming.AuthenticationException;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String message) {
        super(message);
    }
}
