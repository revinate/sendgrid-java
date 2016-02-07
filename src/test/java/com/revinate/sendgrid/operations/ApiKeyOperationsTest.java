package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiKeyOperationsTest {

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    ApiKeyOperations operations;

    @Before
    public void setUp() throws Exception {
        operations = new ApiKeyOperations(client, credential);
    }

    @Test
    public void list_shouldReturnApiKeys() throws Exception {
        String response = readFile("responses/api-keys.json");

        when(client.get("https://api.sendgrid.com/v3/api_keys", credential)).thenReturn(response);

        List<ApiKey> apiKeys = operations.list();

        assertThat(apiKeys, hasSize(2));
        assertThat(apiKeys.get(0).getName(), equalTo("1st API key"));
    }

    @Test(expected = SendGridException.class)
    public void list_shouldThrowSendGridExceptionOnJsonParseException() throws Exception {
        when(client.get("https://api.sendgrid.com/v3/api_keys", credential)).thenReturn("not a json");

        operations.list();
    }

    @Test
    public void retrieve_shouldReturnApiKey() throws Exception {
        String apiKeyId = "sdaspfgada5hahsrs5hSHF";
        String response = readFile("responses/api-key.json");

        when(client.get("https://api.sendgrid.com/v3/api_keys/" + apiKeyId, credential)).thenReturn(response);

        ApiKey apiKey = operations.retrieve(apiKeyId);

        assertThat(apiKey, notNullValue());
        assertThat(apiKey.getName(), equalTo("1st API key"));
        assertThat(apiKey.getApiKeyId(), equalTo(apiKeyId));
    }

    @Test
    public void create_shouldPostAndReturnApiKey() throws Exception {
        String apiKeyId = "sdaspfgada5hahsrs5hSHF";
        String response = readFile("responses/api-key.json");
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        String requestBody = ApiKeyOperations.OBJECT_MAPPER.writeValueAsString(apiKey);

        when(client.post("https://api.sendgrid.com/v3/api_keys", requestBody,
                "application/json", credential)).thenReturn(response);

        ApiKey apiKey1 = operations.create(apiKey);

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("1st API key"));
        assertThat(apiKey1.getApiKeyId(), equalTo(apiKeyId));
    }

    private static String readFile(String path) throws IOException {
        return IOUtils.toString(ApiKeyOperationsTest.class.getClassLoader().getResourceAsStream(path));
    }
}
