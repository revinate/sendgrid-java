package com.revinate.sendgrid.model;

import java.util.List;

public class ApiErrorsResponse extends SendGridResource {

    private List<ApiError> errors;

    public List<ApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        if (errors == null || errors.isEmpty()) {
            return "No errors";
        } else if (errors.size() == 1) {
            return errors.get(0).toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple errors:\n");
            for (ApiError error : errors) {
                sb.append("\t").append(error.toString()).append("\n");
            }
            return sb.toString();
        }
    }
}
