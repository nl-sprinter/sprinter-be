package com.nl.sprinterbe.exception;

import javax.naming.AuthenticationException;

public class LoginFormException extends AuthenticationException {
    public LoginFormException(String msg) {
        super(msg);
    }

}
