package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.UnsupportedOperationException;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public abstract class SendGridResource {

    protected final String baseUrl;
    protected final SendGridHttpClient client;
    protected final Credential credential;

    protected enum ApiVersion {
        V2("api"),
        V2A("apiv2"),
        V3("v3");

        private final String urlSegment;

        ApiVersion(String urlSegment) {
            this.urlSegment = urlSegment;
        }

        public String toUrlSegment() {
            return urlSegment;
        }
    }

    public SendGridResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.credential = credential;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public SendGridHttpClient getClient() {
        return client;
    }

    public Credential getCredential() {
        return credential;
    }

    protected String getApiUrl(ApiVersion apiVersion) {
        return String.format("%s/%s", baseUrl, apiVersion.toUrlSegment());
    }

    protected UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException("Operation not supported on this resource");
    }
}
