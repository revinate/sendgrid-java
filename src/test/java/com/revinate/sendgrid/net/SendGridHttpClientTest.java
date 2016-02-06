package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.ApiConnectionException;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
    HttpClient httpClient;

    @Mock
    StringResponseHandler handler;

    SendGridHttpClient client;

    @Before
    public void setUp() throws Exception {
        client = new SendGridHttpClient(httpClient, handler);
    }

    @Test
    public void get_shouldAttachCredentialToHeader() throws Exception {
        String expected = "response";
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class))).thenReturn(expected);

        String actual = client.get("http://sendgrid", new ApiKeyCredential("changeme"));

        assertThat(actual, is(expected));

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        ArgumentCaptor<StringResponseHandler> responseFactoryCaptor = ArgumentCaptor.forClass(StringResponseHandler.class);
        verify(httpClient).execute(captor.capture(), responseFactoryCaptor.capture());

        HttpGet httpGet = captor.getValue();
        StringResponseHandler actualResponseFactory = responseFactoryCaptor.getValue();

        assertThat(actualResponseFactory, sameInstance(handler));
        assertThat(httpGet, notNullValue());
        assertThat(httpGet.getURI().toString(), containsString("http://sendgrid"));
        assertThat(Arrays.asList(httpGet.getAllHeaders()), Matchers.<Header>hasItem(hasProperty("name", is("Authorization"))));
    }

    @Test
    public void get_shouldWrapExceptionFromClient() throws Exception {
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class))).thenThrow(new IOException("Unit test"));

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("IOException while making API request to SendGrid.");

        client.get("http://sendgrid", new ApiKeyCredential("changeme"));
    }
}