package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiKeyResourceTest extends BaseSendGridTest {

    private static final String API_KEY_ID = "sdaspfgada5hahsrs5hSHF";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    ApiKeyResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new ApiKeyResource("https://api.sendgrid.com/v3/api_keys", client, credential, API_KEY_ID);
    }

    @Test
    public void retrieve_shouldReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);

        when(client.get("https://api.sendgrid.com/v3/api_keys/" + API_KEY_ID,
                ApiKey.class, credential)).thenReturn(response);

        ApiKey apiKey = resource.retrieve();

        assertThat(apiKey, sameInstance(response));
    }

    @Test
    public void retrieve_shouldHandleMissingId() throws Exception {
        ApiKey apiKey = new ApiKey("1st API key");
        resource = new ApiKeyResource("https://api.sendgrid.com/v3/api_keys", client, credential, apiKey);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.retrieve();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPutAndReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);
        ApiKey apiKey = new ApiKey(response.getName());
        apiKey.setApiKeyId(API_KEY_ID);
        apiKey.setScopes(response.getScopes());

        when(client.put(any(String.class), any(Class.class),
                any(Credential.class), any(ApiKey.class), any(RequestType.class))).thenReturn(response);

        ApiKey apiKey1 = resource.update(apiKey);

        assertThat(apiKey1, sameInstance(response));

        ArgumentCaptor<ApiKey> captor = ArgumentCaptor.forClass(ApiKey.class);
        verify(client).put(eq("https://api.sendgrid.com/v3/api_keys/" + API_KEY_ID),
                eq(ApiKey.class), eq(credential), captor.capture(), eq(RequestType.JSON));

        ApiKey apiKey2 = captor.getValue();

        assertThat(apiKey2, notNullValue());
        assertThat(apiKey2.getName(), equalTo(apiKey.getName()));
        assertThat(apiKey2.getScopes(), equalTo(apiKey.getScopes()));
        assertThat(apiKey2.getApiKeyId(), nullValue());
    }

    @Test
    public void update_shouldHandleMissingId() throws Exception {
        ApiKey apiKey = new ApiKey("1st API key");
        resource = new ApiKeyResource("https://api.sendgrid.com/v3/api_keys", client, credential, apiKey);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.update(apiKey);
    }

    @Test
    public void partialUpdate_shouldPatchAndReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("name", "3rd API key");

        when(client.patch("https://api.sendgrid.com/v3/api_keys/" + API_KEY_ID,
                ApiKey.class, credential, requestObject, RequestType.JSON)).thenReturn(response);

        ApiKey apiKey1 = resource.partialUpdate(requestObject);

        assertThat(apiKey1, sameInstance(response));
    }

    @Test
    public void partialUpdate_shouldHandleMissingId() throws Exception {
        ApiKey apiKey = new ApiKey("1st API key");
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("name", "3rd API key");
        resource = new ApiKeyResource("https://api.sendgrid.com/v3/api_keys", client, credential, apiKey);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.partialUpdate(requestObject);
    }

    @Test
    public void delete_shouldDeleteApiKey() throws Exception {
        resource.delete();

        verify(client).delete("https://api.sendgrid.com/v3/api_keys/" + API_KEY_ID, credential);
    }

    @Test
    public void delete_shouldHandleMissingId() throws Exception {
        ApiKey apiKey = new ApiKey("1st API key");
        resource = new ApiKeyResource("https://api.sendgrid.com/v3/api_keys", client, credential, apiKey);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.delete();
    }
}
