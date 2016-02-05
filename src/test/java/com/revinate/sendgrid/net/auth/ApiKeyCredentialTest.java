package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.junit.Assert.assertThat;

public class ApiKeyCredentialTest {

    @Test
    public void toHttpHeaders_shouldGenerateBearerAuth() throws Exception {
        Header[] headers = new ApiKeyCredential("credential").toHttpHeaders();

        assertThat(headers, arrayWithSize(1));
        assertThat(headers[0].toString(), is("Authorization: Bearer credential"));
    }
}