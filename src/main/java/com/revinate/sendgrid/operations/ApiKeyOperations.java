package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;

import java.io.IOException;
import java.util.List;

public class ApiKeyOperations extends AbstractOperations {

    private final SendGridHttpClient client;
    private final Credential credential;

    public ApiKeyOperations(SendGridHttpClient client, Credential credential) {
        this.client = client;
        this.credential = credential;
    }

    public List<ApiKey> list() throws SendGridException {
        String url = v3Url() + "/api_keys";
        String response = client.get(url, credential);

        ApiKeysResponse apiKeysResponse;
        try {
            apiKeysResponse = JsonUtils.fromJson(response, ApiKeysResponse.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }

        return apiKeysResponse.getResult();
    }

    public ApiKey retrieve(String id) throws SendGridException {
        String url = v3Url() + "/api_keys/" + id;
        String response = client.get(url, credential);

        ApiKey apiKey;
        try {
            apiKey = JsonUtils.fromJson(response, ApiKey.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }

        return apiKey;
    }

    public ApiKey create(ApiKey requestObject) throws SendGridException {
        String url = v3Url() + "/api_keys";

        ApiKey apiKey;
        try {
            String requestBody = JsonUtils.toJson(requestObject);
            String response = client.post(url, requestBody, "application/json", credential);
            apiKey = JsonUtils.fromJson(response, ApiKey.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }

        return apiKey;
    }
}
