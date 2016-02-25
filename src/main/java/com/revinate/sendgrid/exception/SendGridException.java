package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.Collections;
import java.util.List;

public class SendGridException extends Exception {

    private List<ApiError> errors = Collections.emptyList();
    private Integer statusCode;

    public SendGridException(String message) {
        super(message);
    }

    public SendGridException(String message, Throwable e) {
        super(message, e);
    }

    public SendGridException(String message, List<ApiError> errors, Integer statusCode) {
        super(message);
        this.errors = errors;
        this.statusCode = statusCode;
    }

    public List<ApiError> getErrors() {
        return errors;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
