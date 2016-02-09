package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class ApiKeyResource extends EntityResource<ApiKey> {

    public ApiKeyResource(String baseUrl, SendGridHttpClient client, Credential credential, ApiKey apiKey) {
        super(baseUrl, client, credential, ApiKey.class, apiKey);
    }

    public ApiKeyResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, ApiKey.class, id);
    }

    @Override
    public ApiKey update(ApiKey apiKey) throws SendGridException {
        ApiKey requestObject = new ApiKey();
        requestObject.setName(apiKey.getName());
        requestObject.setScopes(apiKey.getScopes());
        return client.put(getUrl(), requestObject, ApiKey.class, credential);
    }
}
