package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;

import java.util.List;

public interface Credential {
    List<Header> toHttpHeaders();
}
