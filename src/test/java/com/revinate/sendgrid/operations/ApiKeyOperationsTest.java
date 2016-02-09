package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
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
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiKeyOperationsTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    ApiKeyOperations operations;

    @Before
    public void setUp() throws Exception {
        operations = new ApiKeyOperations("https://api.sendgrid.com", client, credential);
    }

    @Test
    public void list_shouldReturnApiKeys() throws Exception {
        ApiKeysResponse response = JsonUtils.fromJson(readFile("/responses/api-keys.json"),
                ApiKeysResponse.class);

        when(client.get("https://api.sendgrid.com/v3/api_keys", ApiKeysResponse.class, credential))
                .thenReturn(response);

        List<ApiKey> apiKeys = operations.list();

        assertThat(apiKeys, sameInstance(response.getData()));
    }

    @Test
    public void retrieve_shouldReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);

        when(client.get("https://api.sendgrid.com/v3/api_keys/" + response.getApiKeyId(),
                ApiKey.class, credential)).thenReturn(response);

        ApiKey apiKey = operations.retrieve(response.getApiKeyId());

        assertThat(apiKey, sameInstance(response));
    }

    @Test
    public void create_shouldPostAndReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        apiKey.addScope("mail.send");

        when(client.post("https://api.sendgrid.com/v3/api_keys", apiKey,
                ApiKey.class, credential)).thenReturn(response);

        ApiKey apiKey1 = operations.create(apiKey);

        assertThat(apiKey1, sameInstance(response));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPutAndReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKeyId(response.getApiKeyId());
        apiKey.setName(response.getName());
        apiKey.setScopes(response.getScopes());

        when(client.put(any(String.class), any(ApiKey.class), any(Class.class),
                any(Credential.class))).thenReturn(response);

        ApiKey apiKey1 = operations.update(apiKey);

        assertThat(apiKey1, sameInstance(response));

        ArgumentCaptor<ApiKey> captor = ArgumentCaptor.forClass(ApiKey.class);
        verify(client).put(eq("https://api.sendgrid.com/v3/api_keys/" + response.getApiKeyId()),
                captor.capture(), eq(ApiKey.class), eq(credential));

        ApiKey apiKey2 = captor.getValue();

        assertThat(apiKey2, notNullValue());
        assertThat(apiKey2.getName(), equalTo(apiKey.getName()));
        assertThat(apiKey2.getScopes(), equalTo(apiKey.getScopes()));
        assertThat(apiKey2.getApiKeyId(), nullValue());
    }

    @Test
    public void update_shouldHandleMissingId() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Resource missing identifier");

        operations.update(apiKey);
    }

    @Test
    public void partialUpdate_shouldPatchAndReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKeyId(response.getApiKeyId());
        apiKey.setName(response.getName());
        apiKey.setScopes(response.getScopes());
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("name", "3rd API key");

        when(client.patch("https://api.sendgrid.com/v3/api_keys/" + response.getApiKeyId(),
                requestObject, ApiKey.class, credential)).thenReturn(response);

        ApiKey apiKey1 = operations.partialUpdate(apiKey, requestObject);

        assertThat(apiKey1, sameInstance(response));
    }

    @Test
    public void partialUpdate_shouldHandleMissingId() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("name", "3rd API key");

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Resource missing identifier");

        operations.partialUpdate(apiKey, requestObject);
    }

    @Test
    public void delete_shouldDeleteApiKey() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        apiKey.setApiKeyId("sdaspfgada5hahsrs5hSHF");

        operations.delete(apiKey);

        verify(client).delete("https://api.sendgrid.com/v3/api_keys/" + apiKey.getApiKeyId(), credential);
    }

    @Test
    public void delete_shouldHandleMissingId() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Resource missing identifier");

        operations.delete(apiKey);
    }
}
