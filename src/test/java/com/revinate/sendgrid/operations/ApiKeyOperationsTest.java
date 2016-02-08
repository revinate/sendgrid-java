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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
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

        assertThat(apiKeys, sameInstance(response.getResult()));
    }

    @Test
    public void retrieve_shouldReturnApiKey() throws Exception {
        String apiKeyId = "sdaspfgada5hahsrs5hSHF";
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);

        when(client.get("https://api.sendgrid.com/v3/api_keys/" + apiKeyId, ApiKey.class, credential))
                .thenReturn(response);

        ApiKey apiKey = operations.retrieve(apiKeyId);

        assertThat(apiKey, sameInstance(response));
    }

    @Test
    public void create_shouldPostAndReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");

        when(client.post("https://api.sendgrid.com/v3/api_keys", apiKey,
                ApiKey.class, credential)).thenReturn(response);

        ApiKey apiKey1 = operations.create(apiKey);

        assertThat(apiKey1, sameInstance(response));
    }

    @Test
    public void delete_shouldDeleteApiKey() throws Exception {
        String apiKeyId = "sdaspfgada5hahsrs5hSHF";
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        apiKey.setApiKeyId(apiKeyId);

        operations.delete(apiKey);

        verify(client).delete("https://api.sendgrid.com/v3/api_keys/" + apiKeyId, credential);
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
