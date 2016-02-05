package com.revinate.sendgrid.net.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.junit.Assert.assertThat;

public class UsernamePasswordCredentialTest {

    @Test
    public void toHeaders_shouldGenerateBasicAuth() throws Exception {
        String username = "johndoe";
        String password = "changeme";
        String base64Credentials = Base64.encodeBase64String((username + ":" + password).getBytes());

        Header[] headers = new UsernamePasswordCredential(username, password).toHttpHeaders();

        assertThat(headers, arrayWithSize(1));
        assertThat(headers[0].toString(), is("Authorization: Basic " + base64Credentials));
    }
}