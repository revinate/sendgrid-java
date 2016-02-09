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

    @Override
    public List<Header> toHttpHeaders() {
        return Collections.<Header>singletonList(new BasicHeader("Authorization", "Bearer " + apiKey));
    }
}
