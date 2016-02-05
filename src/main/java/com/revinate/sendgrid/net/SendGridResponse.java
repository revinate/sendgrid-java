package com.revinate.sendgrid.net;

public class SendGridResponse {

    private final String responseBody;
    private final Exception exception;

    public SendGridResponse(String responseBody) {
        this.responseBody = responseBody;
        this.exception = null;
    }

    public SendGridResponse(Exception exception) {
        this.responseBody = null;
        this.exception = exception;
    }

    public boolean isSuccessful() {
        return exception == null;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Exception getException() {
        return exception;
    }
}
