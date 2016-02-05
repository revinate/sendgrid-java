package com.revinate.sendgrid.exception;

public class SendGridException extends Exception {
    public SendGridException(Exception e) {
        super(e);
    }

    public SendGridException(String message, Exception e) {
        super(message, e);
    }
}
