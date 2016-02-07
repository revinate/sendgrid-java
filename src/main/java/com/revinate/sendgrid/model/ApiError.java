package com.revinate.sendgrid.model;

public class ApiError extends SendGridResource {

    private String field;
    private String message;

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
