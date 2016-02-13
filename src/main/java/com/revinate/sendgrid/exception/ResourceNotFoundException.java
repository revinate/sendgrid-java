package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.List;

public class ResourceNotFoundException extends InvalidRequestException {

    public ResourceNotFoundException(String message, List<ApiError> errors, Integer statusCode) {
        super(message, errors, statusCode);
    }
}
