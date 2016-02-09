package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ApiKeyCredentialTest {

    @Test
    public void toHttpHeaders_shouldGenerateBearerAuth() throws Exception {
        List<Header> headers = new ApiKeyCredential("token").toHttpHeaders();

        assertThat(headers, contains(allOf(
                hasProperty("name", equalTo("Authorization")),
                hasProperty("value", equalTo("Bearer token"))
        )));
    }
}
