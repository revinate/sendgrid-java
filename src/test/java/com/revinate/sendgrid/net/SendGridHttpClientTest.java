package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.ApiConnectionException;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class))).thenReturn("response");

        String actual = client.get("http://sendgrid", new ApiKeyCredential("changeme"));

        assertThat(actual, is("response"));

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        ArgumentCaptor<StringResponseHandler> responseHandlerCaptor =
                ArgumentCaptor.forClass(StringResponseHandler.class);
        verify(httpClient).execute(captor.capture(), responseHandlerCaptor.capture());

        HttpGet httpGet = captor.getValue();
        StringResponseHandler actualResponseHandler = responseHandlerCaptor.getValue();

        assertThat(actualResponseHandler, sameInstance(handler));
        assertThat(httpGet, notNullValue());
        assertThat(httpGet.getURI().toString(), containsString("http://sendgrid"));
        assertThat(Arrays.asList(httpGet.getAllHeaders()),
                Matchers.<Header>hasItem(hasProperty("name", is("Authorization"))));
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
        when(httpClient.execute(any(HttpPost.class), any(StringResponseHandler.class))).thenReturn("response");

        String actual = client.post("http://sendgrid", "request", "text/plain", new ApiKeyCredential("changeme"));

        assertThat(actual, is("response"));

        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        ArgumentCaptor<StringResponseHandler> responseHandlerCaptor =
                ArgumentCaptor.forClass(StringResponseHandler.class);
        verify(httpClient).execute(captor.capture(), responseHandlerCaptor.capture());

        HttpPost httpPost = captor.getValue();
        StringResponseHandler actualResponseHandler = responseHandlerCaptor.getValue();

        assertThat(actualResponseHandler, sameInstance(handler));
        assertThat(httpPost, notNullValue());
        assertThat(EntityUtils.toString(httpPost.getEntity()), is("request"));
        assertThat(httpPost.getURI().toString(), containsString("http://sendgrid"));

        List<Header> headers = Arrays.asList(httpPost.getAllHeaders());
        assertThat(headers, Matchers.<Header>hasItem(hasProperty("name", is("Authorization"))));
        assertThat(headers, Matchers.<Header>hasItem(
                allOf(
                        hasProperty("name", is("Content-Type")),
                        hasProperty("value", is("text/plain"))
                )));
    }

    @Test
    public void close_shouldCloseUnderlyingClient() throws Exception {
        client.close();
        verify(httpClient).close();
    }
}
