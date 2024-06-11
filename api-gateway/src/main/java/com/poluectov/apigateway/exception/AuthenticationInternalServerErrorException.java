package com.poluectov.apigateway.exception;

public class AuthenticationInternalServerErrorException extends AbstractStatusCodeAuthenticationException{


    public AuthenticationInternalServerErrorException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
