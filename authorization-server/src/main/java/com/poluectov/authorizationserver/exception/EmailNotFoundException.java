package com.poluectov.authorizationserver.exception;

import org.springframework.http.HttpStatus;

public class EmailNotFoundException extends AbstractAuthenticationException {


    public EmailNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.CONFLICT;
    }
}
