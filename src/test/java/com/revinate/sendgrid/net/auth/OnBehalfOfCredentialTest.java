package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnBehalfOfCredentialTest {

    @Mock
    Credential baseCredential;

    @Test
    @SuppressWarnings("unchecked")
    public void toHttpHeaders_shouldAppendExtraHeader() throws Exception {
        BasicHeader baseAuth = new BasicHeader("Authorization", "Bearer token");
        when(baseCredential.toHttpHeaders()).thenReturn(new Header[]{baseAuth});

        Header[] headers = new OnBehalfOfCredential(baseCredential, "username").toHttpHeaders();

        assertThat(headers, arrayContainingInAnyOrder(
                allOf(
                        hasProperty("name", equalTo("Authorization")),
                        hasProperty("value", equalTo("Bearer token"))
                ),
                allOf(
                        hasProperty("name", equalTo("On-Behalf-Of")),
                        hasProperty("value", equalTo("username"))
                )
        ));
    }
}
