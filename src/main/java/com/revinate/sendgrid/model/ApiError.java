package com.revinate.sendgrid.model;

public class ApiError extends SendGridModel {

    private String field;
    private String message;

    public ApiError() {
        // no args constructor for Jackson
    }

    public ApiError(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return field == null ? message : "field \"" + field + "\": " + message;
    }
}
