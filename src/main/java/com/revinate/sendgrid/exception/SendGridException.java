package com.revinate.sendgrid.exception;

public class SendGridException extends Exception {

    public SendGridException(String message) {
        super(message);
    }

    public SendGridException(String message, Throwable e) {
        super(message, e);
    }
}
