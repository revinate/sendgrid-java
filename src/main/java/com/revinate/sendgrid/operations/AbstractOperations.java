package com.revinate.sendgrid.operations;

public class AbstractOperations {

    protected String baseUrl() {
        return "https://api.sendgrid.com";
    }

    protected String v3Url() {
        return baseUrl() + "/v3";
    }
}
