package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class RootResource extends SendGridResource {

    public RootResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public ApiKeyResource apiKeys() {
        return new ApiKeyResource(baseUrl, client, credential);
    }

    public IpResource ips() {
        return new IpResource(baseUrl, client, credential);
    }

    public IpPoolResource ipPools() {
        return new IpPoolResource(baseUrl, client, credential);
    }

    public SubuserResource subusers() {
        return new SubuserResource(baseUrl, client, credential);
    }

    @Override
    protected ApiVersion getApiVersion() {
        return null;
    }

    @Override
    protected String getEndpoint() {
        return "";
    }
}
