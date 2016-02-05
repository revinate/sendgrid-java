package com.revinate.sendgrid.exception;

public class NotFoundException extends InvalidRequestException {

    public NotFoundException(String message) {
        super(message);
    }
}
