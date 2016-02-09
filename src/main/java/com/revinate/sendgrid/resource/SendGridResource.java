package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Identifiable;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public abstract class SendGridResource {

    protected final String url;
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

    public SendGridResource(String url, SendGridHttpClient client, Credential credential) {
        this.url = url;
        this.client = client;
        this.credential = credential;
    }

    public String getUrl() {
        return url;
    }

    public SendGridHttpClient getClient() {
        return client;
    }

    public Credential getCredential() {
        return credential;
    }

    protected String getCollectionUrl(String endpoint) {
        return String.format("%s/%s", url, endpoint);
    }

    protected String getCollectionUrl(ApiVersion apiVersion, String endpoint) {
        return String.format("%s/%s/%s", url, apiVersion.toUrlSegment(), endpoint);
    }

    protected String getObjectUrl(Identifiable resource) throws InvalidRequestException {
        return getObjectUrl(resource.getPathId());
    }

    protected String getObjectUrl(String id) throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException("Missing object identifier");
        }
        return String.format("%s/%s", url, id);
    }
}
