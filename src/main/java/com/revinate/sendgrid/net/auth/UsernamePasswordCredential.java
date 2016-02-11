package com.revinate.sendgrid.net.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
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
                new BasicHeader(HttpHeaders.AUTHORIZATION, "Basic " + base64Credential()));
    }

    private String base64Credential() {
        return Base64.encodeBase64String((username + ":" + password).getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsernamePasswordCredential)) return false;

        UsernamePasswordCredential that = (UsernamePasswordCredential) o;

        if (username == null ? that.username != null : !username.equals(that.username)) return false;
        return password == null ? that.password == null : password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = username == null ? 0 : username.hashCode();
        result = 31 * result + (password == null ? 0 : password.hashCode());
        return result;
    }
}
