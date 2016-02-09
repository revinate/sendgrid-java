package com.revinate.sendgrid.net.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.Collections;
import java.util.List;

public class UsernamePasswordCredential implements Credential {

    private final String username;
    private final String password;

    public UsernamePasswordCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public List<Header> toHttpHeaders() {
        return Collections.<Header>singletonList(
                new BasicHeader("Authorization", "Basic " + base64Credential()));
    }

    private String base64Credential() {
        return Base64.encodeBase64String((username + ":" + password).getBytes());
    }
}
