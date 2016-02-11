package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

public class OnBehalfOfCredential implements Credential {

    public static final String ON_BEHALF_OF = "On-Behalf-Of";

    private final Credential credential;
    private final String username;

    public OnBehalfOfCredential(Credential credential, String username) {
        this.credential = credential;
        this.username = username;
    }

    public Credential getCredential() {
        return credential;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public List<Header> toHttpHeaders() {
        List<Header> headers = new ArrayList<Header>(credential.toHttpHeaders());
        headers.add(new BasicHeader(ON_BEHALF_OF, username));
        return headers;
    }
}
