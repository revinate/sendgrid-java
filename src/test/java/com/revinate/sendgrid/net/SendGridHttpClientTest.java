package com.revinate.sendgrid.net;

import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SendGridHttpClientTest {

    @Mock
    HttpClient httpClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    HttpResponse httpResponse;

    @Mock
    HttpResponseReader responseReader;

    SendGridHttpClient client;

    @Before
    public void setUp() throws Exception {
        client = new SendGridHttpClient(httpClient);
        client.setResponseReader(responseReader);
    }

    @Test
    public void get_shouldAttachCredentialToHeader() throws Exception {
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        HttpEntity entity = httpResponse.getEntity();
        when(responseReader.readContent(entity)).thenReturn("response");

        String response = client.get("http://sendgrid", new ApiKeyCredential("changeme"));

        assertThat(response, containsString("response"));

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(captor.capture());

        HttpGet httpGet = captor.getValue();

        assertThat(httpGet, notNullValue());
        assertThat(httpGet.getURI().toString(), containsString("http://sendgrid"));
        assertThat(Arrays.asList(httpGet.getAllHeaders()), Matchers.<Header>hasItem(hasProperty("name", is("Authorization"))));
    }
}