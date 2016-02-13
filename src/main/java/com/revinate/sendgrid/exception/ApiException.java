package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.Collections;
import java.util.List;

public class ApiException extends SendGridException {

    private List<ApiError> errors = Collections.emptyList();
    private Integer statusCode;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable e) {
        super(message, e);
    }

    public ApiException(String message, List<ApiError> errors, Integer statusCode) {
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
