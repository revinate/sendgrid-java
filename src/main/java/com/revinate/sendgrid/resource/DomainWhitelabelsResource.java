package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.Whitelabel;
import com.revinate.sendgrid.model.WhitelabelCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class DomainWhitelabelsResource extends CollectionResource<Whitelabel, WhitelabelCollection> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "whitelabel/domains";

    public DomainWhitelabelsResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, Whitelabel.class, WhitelabelCollection.class);
    }

    public DomainWhitelabelResource entity(Whitelabel whitelabel) {
        return new DomainWhitelabelResource(getUrl(), client, credential, whitelabel);
    }

    public DomainWhitelabelResource entity(String id) {
        return new DomainWhitelabelResource(getUrl(), client, credential, id);
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
