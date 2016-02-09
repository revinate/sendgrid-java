package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public abstract class RootResource {

    public abstract String getUrl();

    public abstract SendGridHttpClient getClient();

    public abstract Credential getCredential();

    public ApiKeyResource apiKeys() {
        return new ApiKeyResource(getUrl(), getClient(), getCredential());
    }

    public IpResource ips() {
        return new IpResource(getUrl(), getClient(), getCredential());
    }

    public IpPoolResource ipPools() {
        return new IpPoolResource(getUrl(), getClient(), getCredential());
    }

    public SubuserResource subusers() {
        return new SubuserResource(getUrl(), getClient(), getCredential());
    }
}
