package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.Arrays;

public class OnBehalfOfCredential implements Credential {
    private final Credential credential;
    private final String subuserName;

    public OnBehalfOfCredential(Credential credential, String subuserName) {
        this.credential = credential;
        this.subuserName = subuserName;
    }

    @Override
    public Header[] toHttpHeaders() {
        Header[] headers = credential.toHttpHeaders();
        int length = headers.length;
        Header[] newHeaders = Arrays.copyOf(headers, length + 1);
        newHeaders[length] = new BasicHeader("On-Behalf-Of", subuserName);
        return newHeaders;
    }
}
