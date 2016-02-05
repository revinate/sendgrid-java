package com.revinate.sendgrid.exception;

public class InvalidRequestException extends SendGridException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
