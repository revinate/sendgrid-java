package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;

public class ApiKeyOperations extends SendGridOperations {

    private final SendGridHttpClient client;
    private final Credential credential;

    public ApiKeyOperations(SendGridHttpClient client, Credential credential) {
        this.client = client;
        this.credential = credential;
    }

    public List<ApiKey> list() throws SendGridException {
        String url = v3Url() + "/api_keys";
        ApiKeysResponse apiKeysResponse = client.get(url, ApiKeysResponse.class, credential);
        return apiKeysResponse.getResult();
    }

    public ApiKey retrieve(String id) throws SendGridException {
        String url = v3Url() + "/api_keys/" + id;
        return client.get(url, ApiKey.class, credential);
    }

    public ApiKey create(ApiKey requestObject) throws SendGridException {
        String url = v3Url() + "/api_keys";
        return client.post(url, requestObject, ApiKey.class, credential);
    }
}
