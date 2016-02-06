package com.revinate.sendgrid.net;

import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    @Mock
    HttpClient httpClient;

    @Mock
    SendGridResponseFactory responseFactory;

    SendGridHttpClient client;

    @Before
    public void setUp() throws Exception {
        client = new SendGridHttpClient(httpClient, responseFactory);
    }

    @Test
    public void get_shouldAttachCredentialToHeader() throws Exception {
        SendGridResponse mockResponse = new SendGridResponse("response");

        when(httpClient.execute(any(HttpGet.class), any(ResponseHandler.class))).thenReturn(mockResponse);

        SendGridResponse response = client.get("http://sendgrid", new ApiKeyCredential("changeme"));

        assertThat(response, sameInstance(mockResponse));

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));

        HttpGet httpGet = captor.getValue();

        assertThat(httpGet, notNullValue());
        assertThat(httpGet.getURI().toString(), containsString("http://sendgrid"));
        assertThat(Arrays.asList(httpGet.getAllHeaders()), Matchers.<Header>hasItem(hasProperty("name", is("Authorization"))));
    }

    // TODO validate exception, it should catch IOException and return response
}