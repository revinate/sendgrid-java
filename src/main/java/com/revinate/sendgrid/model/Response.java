package com.revinate.sendgrid.model;

import java.util.List;

public class Response extends SendGridModel {

    private String message;
    private List<String> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        if (errors == null || errors.isEmpty()) {
            return message;
        } else if (errors.size() == 1) {
            return message + ": " + errors.get(0);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(message).append(":\n");
            for (String error : errors) {
                sb.append("\t").append(error).append("\n");
            }
            return sb.toString();
        }
    }
}
