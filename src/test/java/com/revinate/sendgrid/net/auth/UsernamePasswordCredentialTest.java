package com.revinate.sendgrid.net.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UsernamePasswordCredentialTest {

    @Test
    public void toHeaders_shouldGenerateBasicAuth() throws Exception {
        String username = "username";
        String password = "password";
        String base64Credentials = Base64.encodeBase64String((username + ":" + password).getBytes());

        List<Header> headers = new UsernamePasswordCredential(username, password).toHttpHeaders();

        assertThat(headers, contains(allOf(
                hasProperty("name", equalTo("Authorization")),
                hasProperty("value", equalTo("Basic " + base64Credentials))
        )));
    }
}