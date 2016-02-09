package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public abstract class SendGridOperationsProvider {

    public abstract String getUrl();

    public abstract SendGridHttpClient getClient();

    public abstract Credential getCredential();

    public ApiKeyOperations apiKeys() {
        return new ApiKeyOperations(getUrl(), getClient(), getCredential());
    }

    public SubuserOperations subusers() {
        return new SubuserOperations(getUrl(), getClient(), getCredential());
    }
}
