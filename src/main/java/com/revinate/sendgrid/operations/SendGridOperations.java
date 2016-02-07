package com.revinate.sendgrid.operations;

public abstract class SendGridOperations {

    protected String baseUrl() {
        return "https://api.sendgrid.com";
    }

    protected String v3Url() {
        return baseUrl() + "/v3";
    }
}
