package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.IpCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;

public class IpResource extends SendGridResource {

    private static final ApiVersion API_VERSION = ApiVersion.V3;
    private static final String ENDPOINT = "ips";

    public IpResource(String url, SendGridHttpClient client, Credential credential) {
        super(url, client, credential);
    }

    public List<Ip> list() throws SendGridException {
        return client.get(getResourceUrl(), IpCollection.class, credential).getData();
    }

    public Ip retrieve(String id) throws SendGridException {
        return client.get(getResourceUrl(id), Ip.class, credential);
    }

    @Override
    protected ApiVersion getApiVersion() {
        return API_VERSION;
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
