package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiKeysResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    ApiKeysResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new ApiKeysResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void entity_shouldReturnResource() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKeyId("test");
        apiKey.setName("1st API key");

        ApiKeyResource subresource = resource.entity(apiKey);

        assertThat(subresource, notNullValue());
        assertThat(subresource.getId(), equalTo("test"));
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/api_keys"));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void list_shouldReturnApiKeys() throws Exception {
        ApiKeysResponse response = JsonUtils.fromJson(readFile("/responses/api-keys.json"),
                ApiKeysResponse.class);

        when(client.get("https://api.sendgrid.com/v3/api_keys", ApiKeysResponse.class, credential))
                .thenReturn(response);

        List<ApiKey> apiKeys = resource.list();

        assertThat(apiKeys, sameInstance(response.getData()));
    }

    @Test
    public void create_shouldPostAndReturnApiKey() throws Exception {
        ApiKey response = JsonUtils.fromJson(readFile("/responses/api-key.json"), ApiKey.class);
        ApiKey apiKey = new ApiKey();
        apiKey.setName("1st API key");
        apiKey.addScope("mail.send");

        when(client.post("https://api.sendgrid.com/v3/api_keys",
                ApiKey.class, credential, apiKey, RequestType.JSON)).thenReturn(response);

        ApiKey apiKey1 = resource.create(apiKey);

        assertThat(apiKey1, sameInstance(response));
    }
}
