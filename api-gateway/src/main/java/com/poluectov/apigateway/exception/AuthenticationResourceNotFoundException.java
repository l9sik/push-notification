package com.poluectov.apigateway.exception;

public class AuthenticationResourceNotFoundException extends AbstractStatusCodeAuthenticationException {


    public AuthenticationResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
