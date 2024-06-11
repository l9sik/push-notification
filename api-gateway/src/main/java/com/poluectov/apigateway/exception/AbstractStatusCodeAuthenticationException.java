package com.poluectov.apigateway.exception;

import javax.naming.AuthenticationException;

public abstract class AbstractStatusCodeAuthenticationException extends AuthenticationException {


    public AbstractStatusCodeAuthenticationException(String message) {
        super(message);
    }
    public abstract int getStatusCode();
}
