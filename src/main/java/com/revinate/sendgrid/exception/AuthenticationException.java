package com.revinate.sendgrid.exception;

import com.revinate.sendgrid.model.ApiError;

import java.util.List;

public class AuthenticationException extends InvalidRequestException {

    public AuthenticationException(String message, List<ApiError> errors, Integer statusCode) {
        super(message, errors, statusCode);
    }
}
