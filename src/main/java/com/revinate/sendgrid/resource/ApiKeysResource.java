package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class ApiKeysResource extends CollectionResource<ApiKey, ApiKeysResponse> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "api_keys";

    public ApiKeysResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, ApiKey.class, ApiKeysResponse.class);
    }

    public ApiKeyResource entity(ApiKey apiKey) throws InvalidRequestException {
        return new ApiKeyResource(getUrl(), client, credential, apiKey);
    }

    public ApiKeyResource entity(String id) throws InvalidRequestException {
        return new ApiKeyResource(getUrl(), client, credential, id);
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
