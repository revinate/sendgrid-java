package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.Collections;
import java.util.List;

public class ApiKeyCredential implements Credential {

    private final String apiKey;

    public ApiKeyCredential(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public List<Header> toHttpHeaders() {
        return Collections.<Header>singletonList(new BasicHeader("Authorization", "Bearer " + apiKey));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiKeyCredential)) return false;

        ApiKeyCredential that = (ApiKeyCredential) o;

        return apiKey == null ? that.apiKey == null : apiKey.equals(that.apiKey);
    }

    @Override
    public int hashCode() {
        return apiKey == null ? 0 : apiKey.hashCode();
    }
}
