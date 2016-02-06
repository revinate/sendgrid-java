package com.revinate.sendgrid.exception;

public class ApiConnectionException extends SendGridException {

    public ApiConnectionException(String message) {
        super(message);
    }

    public ApiConnectionException(String message, Throwable e) {
        super(message, e);
    }
}
