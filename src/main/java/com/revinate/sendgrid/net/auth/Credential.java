package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;

public interface Credential {
    Header[] toHttpHeaders();
}
