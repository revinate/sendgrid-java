package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class RootResource extends SendGridResource {

    public RootResource(String url, SendGridHttpClient client, Credential credential) {
        super(url, client, credential);
    }

    public ApiKeyResource apiKeys() {
        return new ApiKeyResource(getCollectionUrl(ApiKeyResource.API_VERSION,
                ApiKeyResource.ENDPOINT), client, credential);
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
