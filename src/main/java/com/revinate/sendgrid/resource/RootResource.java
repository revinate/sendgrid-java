package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class RootResource extends SendGridResource {

    public RootResource(String url, SendGridHttpClient client, Credential credential) {
        super(url, client, credential);
    }

    public ApiKeyResource apiKeys() {
        return new ApiKeyResource(url, client, credential);
    }

    public IpResource ips() {
        return new IpResource(url, client, credential);
    }

    public IpPoolResource ipPools() {
        return new IpPoolResource(url, client, credential);
    }

    public SubuserResource subusers() {
        return new SubuserResource(url, client, credential);
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
