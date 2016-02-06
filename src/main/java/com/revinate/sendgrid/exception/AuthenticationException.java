package com.revinate.sendgrid.exception;

public class AuthenticationException extends InvalidRequestException {

    public AuthenticationException(String message) {
        super(message);
    }
}
