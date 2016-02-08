package com.revinate.sendgrid.net;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.ApiConnectionException;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
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

        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey = client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));

        assertThat(apiKey, notNullValue());
        assertThat(apiKey.getName(), equalTo("1st API key"));
        assertThat(apiKey.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

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
        thrown.expectMessage("HTTP protocol error while making API request to SendGrid");

        client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));
    }

    @Test
    public void get_shouldWrapConnectionIOException() throws Exception {
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class)))
                .thenThrow(new IOException("Unit test"));

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("IOException while making API request to SendGrid");

        client.get("http://sendgrid", ApiKey.class, new ApiKeyCredential("token"));
    }

    @Test
    public void get_shouldWrapMappingIOException() throws Exception {
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class)))
                .thenReturn("not a json");

        thrown.expect(ApiConnectionException.class);
        thrown.expectMessage("IOException while mapping response");

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

        when(httpClient.execute(any(HttpPost.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey1 = client.post("http://sendgrid", apiKey, ApiKey.class,
                new ApiKeyCredential("token"));

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("1st API key"));
        assertThat(apiKey1.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpPost httpPost = captor.getValue();

        assertThat(httpPost, notNullValue());
        assertThat(EntityUtils.toString(httpPost.getEntity()), equalTo(request));
        assertThat(httpPost.getURI(), hasToString("http://sendgrid"));
        assertThat(httpPost.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpPost.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("application/json"))
                )
        ));
    }

    @Test
    public void post_shouldHandleEmptyRequest() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Request object is null");
        client.post("http://sendgrid", null, ApiKey.class, new ApiKeyCredential("token"));
    }

    @Test
    public void put_shouldMakeRequestAndReturnResponse() throws Exception {
        String response = readFile("/responses/api-key.json");
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        apiKey.addScope("mail.send");
        String request = JsonUtils.toJson(apiKey);

        when(httpClient.execute(any(HttpPut.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey1 = client.put("http://sendgrid", apiKey, ApiKey.class,
                new ApiKeyCredential("token"));

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("1st API key"));
        assertThat(apiKey1.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpPut httpPut = captor.getValue();

        assertThat(httpPut, notNullValue());
        assertThat(EntityUtils.toString(httpPut.getEntity()), equalTo(request));
        assertThat(httpPut.getURI(), hasToString("http://sendgrid"));
        assertThat(httpPut.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpPut.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("application/json"))
                )
        ));
    }

    @Test
    public void patch_shouldMakeRequestAndReturnResponse() throws Exception {
        String response = readFile("/responses/api-key.json");
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        String request = JsonUtils.toJson(apiKey);

        when(httpClient.execute(any(HttpPatch.class), any(StringResponseHandler.class)))
                .thenReturn(response);

        ApiKey apiKey1 = client.patch("http://sendgrid", apiKey, ApiKey.class,
                new ApiKeyCredential("token"));

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("1st API key"));
        assertThat(apiKey1.getApiKeyId(), equalTo("sdaspfgada5hahsrs5hSHF"));

        ArgumentCaptor<HttpPatch> captor = ArgumentCaptor.forClass(HttpPatch.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpPatch httpPatch = captor.getValue();

        assertThat(httpPatch, notNullValue());
        assertThat(EntityUtils.toString(httpPatch.getEntity()), equalTo(request));
        assertThat(httpPatch.getURI(), hasToString("http://sendgrid"));
        assertThat(httpPatch.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
        assertThat(httpPatch.getAllHeaders(), hasItemInArray(
                allOf(
                        hasProperty("name", equalTo("Content-Type")),
                        hasProperty("value", equalTo("application/json"))
                )
        ));
    }

    @Test
    public void delete_shouldMakeRequest() throws Exception {
        client.delete("http://sendgrid", new ApiKeyCredential("token"));

        ArgumentCaptor<HttpDelete> captor = ArgumentCaptor.forClass(HttpDelete.class);
        verify(httpClient).execute(captor.capture(), eq(handler));

        HttpDelete httpDelete = captor.getValue();

        assertThat(httpDelete, notNullValue());
        assertThat(httpDelete.getURI(), hasToString("http://sendgrid"));
        assertThat(httpDelete.getAllHeaders(), hasItemInArray(
                hasProperty("name", equalTo("Authorization"))
        ));
    }

    @Test
    public void close_shouldCloseUnderlyingClient() throws Exception {
        client.close();
        verify(httpClient).close();
    }
}
