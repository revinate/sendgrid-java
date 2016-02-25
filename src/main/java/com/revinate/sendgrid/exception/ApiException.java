package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.List;

public class ApiException extends SendGridException {

    public ApiException(String message, List<ApiError> errors, Integer statusCode) {
        super(message, errors, statusCode);
    }
}
