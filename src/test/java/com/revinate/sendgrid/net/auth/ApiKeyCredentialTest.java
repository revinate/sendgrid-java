package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ApiKeyCredentialTest {

    @Test
    @SuppressWarnings("unchecked")
    public void toHttpHeaders_shouldGenerateBearerAuth() throws Exception {
        Header[] headers = new ApiKeyCredential("token").toHttpHeaders();

        assertThat(headers, array(allOf(
                hasProperty("name", equalTo("Authorization")),
                hasProperty("value", equalTo("Bearer token"))
        )));
    }
}
