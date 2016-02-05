package com.revinate.sendgrid.exception;

import com.sun.xml.internal.ws.client.SenderException;

import java.io.IOException;

public class HttpException extends SenderException {
    public HttpException(IOException e) {
        super("IO exception reaching SendGrid", e);
    }
}
