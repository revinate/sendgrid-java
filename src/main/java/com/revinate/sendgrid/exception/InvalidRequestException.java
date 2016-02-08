package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.Collections;
import java.util.List;

public class InvalidRequestException extends SendGridException {

    private List<ApiError> errors = Collections.emptyList();

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable e) {
        super(message, e);
    }

    public InvalidRequestException(String message, List<ApiError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ApiError> getErrors() {
        return errors;
    }
}
