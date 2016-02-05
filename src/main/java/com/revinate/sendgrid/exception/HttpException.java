package com.revinate.sendgrid.exception;

import java.io.IOException;

public class HttpException extends SendGridException {
    public HttpException(IOException e) {
        super("IO exception reaching SendGrid", e);
    }
}
