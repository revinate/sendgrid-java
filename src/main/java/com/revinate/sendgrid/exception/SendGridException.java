package com.revinate.sendgrid.exception;

public class SendGridException extends Exception {
    public SendGridException(Exception e) {
        super(e);
    }
}
