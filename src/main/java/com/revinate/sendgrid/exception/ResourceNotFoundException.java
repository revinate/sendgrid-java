package com.revinate.sendgrid.exception;

public class ResourceNotFoundException extends InvalidRequestException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
