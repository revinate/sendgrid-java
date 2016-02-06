package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.io.IOException;
import java.util.List;

public class ApiKeyOperations extends AbstractOperations {

    private final SendGridHttpClient client;
    private final Credential credential;

    public ApiKeyOperations(SendGridHttpClient client, Credential credential) {
        this.client = client;
        this.credential = credential;
    }

    public List<ApiKey> getAll() throws SendGridException {
        String url = v3Url() + "/api_keys";
        String response = client.get(url, credential);

        ApiKeysResponse apiKeysResponse;
        try {
            apiKeysResponse = OBJECT_MAPPER.readValue(response, ApiKeysResponse.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }

        return apiKeysResponse.getResult();
    }
}
