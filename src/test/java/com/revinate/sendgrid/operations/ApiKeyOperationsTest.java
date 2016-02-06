package com.revinate.sendgrid.operations;

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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
    public void getAll_shouldCallClient() throws Exception {
        String response = readFile("responses/api-keys-result.json");

        when(client.get("https://api.sendgrid.com/v3/api_keys", credential)).thenReturn(response);

        List<ApiKey> apiKeys = operations.getAll();

        assertThat(apiKeys, hasSize(2));
        assertThat(apiKeys.get(0).getName(), is("1st API key"));
    }

    private static String readFile(String path) throws IOException {
        return IOUtils.toString(ApiKeyOperationsTest.class.getClassLoader().getResourceAsStream(path));
    }
}