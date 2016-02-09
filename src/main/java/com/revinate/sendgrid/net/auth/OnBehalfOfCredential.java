package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

public class OnBehalfOfCredential implements Credential {

    private final Credential credential;
    private final String username;

    public OnBehalfOfCredential(Credential credential, String username) {
        this.credential = credential;
        this.username = username;
    }

    @Override
    public List<Header> toHttpHeaders() {
        List<Header> headers = new ArrayList<Header>(credential.toHttpHeaders());
        headers.add(new BasicHeader("On-Behalf-Of", username));
        return headers;
    }
}
