package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Identifiable;
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

        private String urlSegment;

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

    abstract protected ApiVersion getApiVersion();

    abstract protected String getEndpoint();

    protected String getResourceUrl() {
        return String.format("%s/%s/%s", baseUrl, getApiVersion().toUrlSegment(), getEndpoint());
    }

    protected String getResourceUrl(Identifiable resource) throws InvalidRequestException {
        return getResourceUrl(resource.getPathId());
    }

    protected String getResourceUrl(String id) throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException("Missing object identifier");
        }
        return String.format("%s/%s/%s/%s", baseUrl, getApiVersion().toUrlSegment(), getEndpoint(), id);
    }

    protected String getResourceUrl(Identifiable resource, String endpoint) throws InvalidRequestException {
        return getResourceUrl(resource.getPathId(), endpoint);
    }

    protected String getResourceUrl(String id, String endpoint) throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException("Missing object identifier");
        }
        if (endpoint == null) {
            throw new InvalidRequestException("Missing subresource endpoint");
        }
        return String.format("%s/%s/%s/%s/%s", baseUrl, getApiVersion().toUrlSegment(), getEndpoint(), id, endpoint);
    }
}
