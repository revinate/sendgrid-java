package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;

public class ApiKeyOperations extends SendGridOperations {

    private static final ApiVersion API_VERSION = ApiVersion.V3;
    private static final String ENDPOINT = "api_keys";

    public ApiKeyOperations(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public List<ApiKey> list() throws SendGridException {
        return client.get(getResourceUrl(), ApiKeysResponse.class, credential).getResult();
    }

    public ApiKey retrieve(String id) throws SendGridException {
        return client.get(getResourceUrl(id), ApiKey.class, credential);
    }

    public ApiKey create(ApiKey requestObject) throws SendGridException {
        return client.post(getResourceUrl(), requestObject, ApiKey.class, credential);
    }

    public void delete(ApiKey apiKey) throws SendGridException {
        client.delete(getResourceUrl(apiKey), credential);
    }

    @Override
    protected ApiVersion getApiVersion() {
        return API_VERSION;
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
