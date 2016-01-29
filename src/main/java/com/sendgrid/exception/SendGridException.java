package com.sendgrid.exception;

public class SendGridException extends Exception {
    public SendGridException(Exception e) {
        super(e);
    }
}
