package com.revinate.sendgrid.net;

import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
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
    public void get_shouldSetHeadersWithUsernamePassword() throws Exception {
        String username = "johndoe";
        String password = "changeme";
        String base64Credentials = Base64.encodeBase64String((username + ":" + password).getBytes());

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        HttpEntity entity = httpResponse.getEntity();
        when(responseReader.readContent(entity)).thenReturn("response");

        String response = client.get("http://sendgrid", new UsernamePasswordCredential(username, password));

        assertThat(response, containsString("response"));

        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(captor.capture());

        HttpGet httpGet = captor.getValue();

        assertThat(httpGet, notNullValue());
        assertThat(httpGet.getURI().toString(), containsString("http://sendgrid"));
        assertThat(httpGet.getAllHeaders(), arrayWithSize(1));
        assertThat(httpGet.getAllHeaders()[0].toString(), is("Authorization: Basic " + base64Credentials));
    }

    @Test
    public void get_shouldSetHeadersWithApiKey() throws Exception {
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
        assertThat(httpGet.getAllHeaders(), arrayWithSize(1));
        assertThat(httpGet.getAllHeaders()[0].toString(), is("Authorization: Bearer changeme"));
    }
}