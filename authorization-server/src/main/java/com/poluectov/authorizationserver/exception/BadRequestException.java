package com.poluectov.authorizationserver.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String s) {
        super(s);
    }
}
