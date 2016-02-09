package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.IpCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;

public class IpOperations extends SendGridOperations {

    private static final ApiVersion API_VERSION = ApiVersion.V3;
    private static final String ENDPOINT = "ips";

    public IpOperations(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
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
