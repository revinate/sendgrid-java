package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.List;

public class InvalidRequestException extends ApiException {

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable e) {
        super(message, e);
    }

    public InvalidRequestException(String message, List<ApiError> errors, Integer statusCode) {
        super(message, errors, statusCode);
    }
}
