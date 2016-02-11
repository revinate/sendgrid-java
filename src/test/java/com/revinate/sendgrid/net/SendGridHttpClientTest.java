package com.revinate.sendgrid.net;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.ApiConnectionException;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
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
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SendGridHttpClientTest extends BaseSendGridTest {

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
        String response = readFile("/responses/api-key.json");

        when(httpClient.execute(any(HttpUriRequest.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey = client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));

        assertThat(apiKey, notNullValue());
        assertThat(apiKey.getName(), equalTo("1st API key"));
        assertThat(apiKey.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpUriRequest httpRequest = captor.getValue();

        assertThat(httpRequest, notNullValue());
        assertThat(httpRequest.getMethod(), equalTo("GET"));
        assertThat(httpRequest.getURI(), hasToString("http://sendgrid"));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
    }

    @Test
    public void get_shouldWrapClientProtocolException() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class), any(StringResponseHandler.class)))
                .thenThrow(new ClientProtocolException("Unit test"));

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("HTTP protocol error while making API request to SendGrid");

        client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));
    }

    @Test
    public void get_shouldWrapConnectionIOException() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class), any(StringResponseHandler.class)))
                .thenThrow(new IOException("Unit test"));

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("I/O error while making API request to SendGrid");

        client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));
    }

    @Test
    public void get_shouldWrapMappingIOException() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class), any(StringResponseHandler.class)))
                .thenReturn("not a json");

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("Error while mapping response");

        client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));
    }

    @Test
    public void get_shouldHandleEmptyResponse() throws Exception {
        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("Response contains no content");

        client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));
    }

    @Test
    public void post_shouldMakeRequestAndReturnResponse() throws Exception {
        String response = readFile("/responses/api-key.json");
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        apiKey.addScope("mail.send");
        String request = JsonUtils.toJson(apiKey);

        when(httpClient.execute(any(HttpUriRequest.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey1 = client.post("http://sendgrid", ApiKey.class,
                new ApiKeyCredential("token"), apiKey, RequestType.JSON);

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("1st API key"));
        assertThat(apiKey1.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

        ArgumentCaptor<HttpEntityEnclosingRequestBase> captor = ArgumentCaptor
                .forClass(HttpEntityEnclosingRequestBase.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpEntityEnclosingRequestBase httpRequest = captor.getValue();

        assertThat(httpRequest, notNullValue());
        assertThat(httpRequest.getMethod(), equalTo("POST"));
        assertThat(EntityUtils.toString(httpRequest.getEntity()), equalTo(request));
        assertThat(httpRequest.getURI(), hasToString("http://sendgrid"));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("application/json"))
                )
        ));
    }

    @Test
    public void post_shouldHandleEmptyRequest() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Error while creating request entity");
        client.post("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"), null, RequestType.JSON);
    }

    @Test
    public void put_shouldMakeRequestAndReturnResponse() throws Exception {
        String response = readFile("/responses/api-key.json");
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        apiKey.addScope("mail.send");
        String request = JsonUtils.toJson(apiKey);

        when(httpClient.execute(any(HttpUriRequest.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey1 = client.put("http://sendgrid", ApiKey.class,
                new ApiKeyCredential("token"), apiKey, RequestType.JSON);

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("1st API key"));
        assertThat(apiKey1.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

        ArgumentCaptor<HttpEntityEnclosingRequestBase> captor = ArgumentCaptor
                .forClass(HttpEntityEnclosingRequestBase.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpEntityEnclosingRequestBase httpRequest = captor.getValue();

        assertThat(httpRequest, notNullValue());
        assertThat(httpRequest.getMethod(), equalTo("PUT"));
        assertThat(EntityUtils.toString(httpRequest.getEntity()), equalTo(request));
        assertThat(httpRequest.getURI(), hasToString("http://sendgrid"));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("application/json"))
                )
        ));
    }

    @Test
    public void patch_shouldMakeRequestAndReturnResponse() throws Exception {
        String response = readFile("/responses/api-key.json");
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("name", "1st API key");
        String request = JsonUtils.toJson(requestObject);

        when(httpClient.execute(any(HttpUriRequest.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey1 = client.patch("http://sendgrid", ApiKey.class,
                new ApiKeyCredential("token"), requestObject, RequestType.JSON);

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("1st API key"));
        assertThat(apiKey1.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

        ArgumentCaptor<HttpEntityEnclosingRequestBase> captor = ArgumentCaptor
                .forClass(HttpEntityEnclosingRequestBase.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpEntityEnclosingRequestBase httpRequest = captor.getValue();

        assertThat(httpRequest, notNullValue());
        assertThat(httpRequest.getMethod(), equalTo("PATCH"));
        assertThat(EntityUtils.toString(httpRequest.getEntity()), equalTo(request));
        assertThat(httpRequest.getURI(), hasToString("http://sendgrid"));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("application/json"))
                )
        ));
    }

    @Test
    public void patch_shouldMakeRequest() throws Exception {
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("name", "1st API key");
        String request = JsonUtils.toJson(requestObject);

        client.patch("http://sendgrid", new ApiKeyCredential("token"), requestObject, RequestType.JSON);

        ArgumentCaptor<HttpEntityEnclosingRequestBase> captor = ArgumentCaptor
                .forClass(HttpEntityEnclosingRequestBase.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpEntityEnclosingRequestBase httpRequest = captor.getValue();

        assertThat(httpRequest, notNullValue());
        assertThat(httpRequest.getMethod(), equalTo("PATCH"));
        assertThat(EntityUtils.toString(httpRequest.getEntity()), equalTo(request));
        assertThat(httpRequest.getURI(), hasToString("http://sendgrid"));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("application/json"))
                )
        ));
    }

    @Test
    public void delete_shouldMakeRequest() throws Exception {
        client.delete("http://sendgrid", new ApiKeyCredential("token"));

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpUriRequest httpRequest = captor.getValue();

        assertThat(httpRequest, notNullValue());
        assertThat(httpRequest.getMethod(), equalTo("DELETE"));
        assertThat(httpRequest.getURI(), hasToString("http://sendgrid"));
        assertThat(httpRequest.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
    }

    @Test
    public void close_shouldCloseUnderlyingClient() throws Exception {
        client.close();
        verify(httpClient).close();
    }
}
