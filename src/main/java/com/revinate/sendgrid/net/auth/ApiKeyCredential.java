package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class ApiKeyCredential implements Credential {
    private String apiKey;

    public ApiKeyCredential(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Header[] toHttpHeaders() {
        return new Header[]{new BasicHeader("Authorization", "Bearer " + apiKey)};
    }
}
