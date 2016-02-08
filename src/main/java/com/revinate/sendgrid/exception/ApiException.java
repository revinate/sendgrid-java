package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.List;

public class ApiException extends SendGridException {

    private List<ApiError> errors;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, List<ApiError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ApiError> getErrors() {
        return errors;
    }
}
