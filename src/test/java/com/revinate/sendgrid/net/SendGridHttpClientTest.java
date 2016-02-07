package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.ApiConnectionException;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SendGridHttpClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    CloseableHttpClient httpClient;

    @Mock
    StringResponseHandler handler;

    SendGridHttpClient client;

    @Before
    public void setUp() throws Exception {
        client = new SendGridHttpClient(httpClient, handler);
    }

    @Test
    public void get_shouldMakeRequestAndReturnResponse() throws Exception {
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class)))
                .thenReturn("response");

        String actual = client.get("http://sendgrid", new ApiKeyCredential("token"));

        assertThat(actual, equalTo("response"));

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpGet httpGet = captor.getValue();

        assertThat(httpGet, notNullValue());
        assertThat(httpGet.getURI(), hasToString("http://sendgrid"));
        assertThat(httpGet.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
    }

    @Test
    public void get_shouldWrapClientProtocolException() throws Exception {
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class)))
                .thenThrow(new ClientProtocolException("Unit test"));

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("Unit test");

        client.get("http://sendgrid", new ApiKeyCredential("changeme"));
    }

    @Test
    public void get_shouldWrapIOException() throws Exception {
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class)))
                .thenThrow(new IOException("Unit test"));

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("IOException while making API request to SendGrid.");

        client.get("http://sendgrid", new ApiKeyCredential("changeme"));
    }

    @Test
    public void post_shouldMakeRequestAndReturnResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class), any(StringResponseHandler.class)))
                .thenReturn("response");

        String actual = client.post("http://sendgrid", "request", "text/plain", new ApiKeyCredential("token"));

        assertThat(actual, equalTo("response"));

        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpPost httpPost = captor.getValue();

        assertThat(httpPost, notNullValue());
        assertThat(EntityUtils.toString(httpPost.getEntity()), equalTo("request"));
        assertThat(httpPost.getURI(), hasToString("http://sendgrid"));
        assertThat(httpPost.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpPost.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("text/plain"))
                )
        ));
    }

    @Test
    public void close_shouldCloseUnderlyingClient() throws Exception {
        client.close();
        verify(httpClient).close();
    }
}
