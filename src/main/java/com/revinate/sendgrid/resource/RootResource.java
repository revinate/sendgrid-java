package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class RootResource extends SendGridResource {

    public RootResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public ApiKeysResource apiKeys() {
        return new ApiKeysResource(getCollectionUrl(ApiKeysResource.API_VERSION,
                ApiKeysResource.ENDPOINT), client, credential);
    }

    public ApiKeyResource apiKey(ApiKey apiKey) throws InvalidRequestException {
        return apiKeys().entity(apiKey);
    }

    public ApiKeyResource apiKey(String id) throws InvalidRequestException {
        return apiKeys().entity(id);
    }

    public IpResource ips() {
        return new IpResource(getCollectionUrl(IpResource.API_VERSION,
                IpResource.ENDPOINT), client, credential);
    }

    public IpPoolResource ipPools() {
        return new IpPoolResource(getCollectionUrl(IpPoolResource.API_VERSION,
                IpPoolResource.ENDPOINT), client, credential);
    }

    public SubuserResource subusers() {
        return new SubuserResource(getCollectionUrl(SubuserResource.API_VERSION,
                SubuserResource.ENDPOINT), client, credential);
    }
}
