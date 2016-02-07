package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.List;

public class InvalidRequestException extends SendGridException {

    private final List<ApiError> errors;

    public InvalidRequestException(String message) {
        super(message);
        this.errors = null;
    }

    public InvalidRequestException(String message, List<ApiError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ApiError> getErrors() {
        return errors;
    }
}
