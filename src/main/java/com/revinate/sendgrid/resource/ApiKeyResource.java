package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.ApiKeysResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;
import java.util.Map;

public class ApiKeyResource extends SendGridResource {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "api_keys";

    public ApiKeyResource(String url, SendGridHttpClient client, Credential credential) {
        super(url, client, credential);
    }

    public List<ApiKey> list() throws SendGridException {
        return client.get(url, ApiKeysResponse.class, credential).getData();
    }

    public ApiKey retrieve(String id) throws SendGridException {
        return client.get(getObjectUrl(id), ApiKey.class, credential);
    }

    public ApiKey create(ApiKey requestObject) throws SendGridException {
        return client.post(url, requestObject, ApiKey.class, credential);
    }

    public ApiKey update(ApiKey apiKey) throws SendGridException {
        ApiKey requestObject = new ApiKey();
        requestObject.setName(apiKey.getName());
        requestObject.setScopes(apiKey.getScopes());
        return client.put(getObjectUrl(apiKey), requestObject, ApiKey.class, credential);
    }

    public ApiKey partialUpdate(ApiKey apiKey, Map<String, Object> requestObject) throws SendGridException {
        return client.patch(getObjectUrl(apiKey), requestObject, ApiKey.class, credential);
    }

    public void delete(ApiKey apiKey) throws SendGridException {
        client.delete(getObjectUrl(apiKey), credential);
    }
}
