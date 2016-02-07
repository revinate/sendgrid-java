package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class ApiKey extends SendGridResource {

    private String name;
    private String apiKeyId;
    private String apiKey;
    private List<String> scopes;

    @Override
    public String getPathId() {
        return apiKeyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKeyId() {
        return apiKeyId;
    }

    public void setApiKeyId(String apiKeyId) {
        this.apiKeyId = apiKeyId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public void addScope(String scope) {
        if (scopes == null) {
            scopes = new ArrayList<String>();
        }
        scopes.add(scope);
    }
}
