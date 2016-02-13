package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.Whitelabel;
import com.revinate.sendgrid.model.WhitelabelCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class LinkWhitelabelsResource extends CollectionResource<Whitelabel, WhitelabelCollection> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "whitelabel/links";

    public LinkWhitelabelsResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, Whitelabel.class, WhitelabelCollection.class);
    }

    public LinkWhitelabelResource entity(Whitelabel whitelabel) {
        return new LinkWhitelabelResource(getUrl(), client, credential, whitelabel);
    }

    public LinkWhitelabelResource entity(String id) {
        return new LinkWhitelabelResource(getUrl(), client, credential, id);
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
