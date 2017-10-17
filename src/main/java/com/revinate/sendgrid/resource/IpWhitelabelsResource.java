package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.IpWhitelabel;
import com.revinate.sendgrid.model.IpWhitelabelCollection;
import com.revinate.sendgrid.model.WhitelabelCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class IpWhitelabelsResource extends CollectionResource<IpWhitelabel, IpWhitelabelCollection> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "whitelabel/ips";

    public IpWhitelabelsResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, IpWhitelabel.class, IpWhitelabelCollection.class);
    }

    public IpWhitelabelResource entity(IpWhitelabel ipWhitelabel) {
        return new IpWhitelabelResource(getUrl(), client, credential, ipWhitelabel);
    }

    public IpWhitelabelResource entity(String id) {
        return new IpWhitelabelResource(getUrl(), client, credential, id);
    }

    @Override
    protected String getEndpoint() { return ENDPOINT; }
}
