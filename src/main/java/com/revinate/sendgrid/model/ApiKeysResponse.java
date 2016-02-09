package com.revinate.sendgrid.model;

import java.util.List;

public class ApiKeysResponse extends SendGridModel implements SendGridCollection<ApiKey> {

    private List<ApiKey> result;

    public List<ApiKey> getResult() {
        return result;
    }

    public void setResult(List<ApiKey> result) {
        this.result = result;
    }

    @Override
    public List<ApiKey> getData() {
        return result;
    }
}
