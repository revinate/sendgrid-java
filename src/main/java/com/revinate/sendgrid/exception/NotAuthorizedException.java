package com.revinate.sendgrid.exception;

public class NotAuthorizedException extends InvalidRequestException {

    public NotAuthorizedException(String message) {
        super(message);
    }
}
