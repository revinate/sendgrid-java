package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.IpCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class IpsResource extends CollectionResource<Ip, IpCollection> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "ips";

    public IpsResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, Ip.class, IpCollection.class);
    }

    public IpResource entity(Ip ip) {
        return new IpResource(getUrl(), client, credential, ip);
    }

    public IpResource entity(String id) {
        return new IpResource(getUrl(), client, credential, id);
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
