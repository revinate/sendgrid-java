package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.IpPool;
import com.revinate.sendgrid.model.IpPoolCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;

public class IpPoolOperations extends SendGridOperations {

    private static final ApiVersion API_VERSION = ApiVersion.V3;
    private static final String ENDPOINT = "ips/pools";

    public IpPoolOperations(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public List<IpPool> list() throws SendGridException {
        return client.get(getResourceUrl(), IpPoolCollection.class, credential).getData();
    }

    public IpPool retrieve(String id) throws SendGridException {
        return client.get(getResourceUrl(id), IpPool.class, credential);
    }

    public IpPool create(IpPool requestObject) throws SendGridException {
        return client.post(getResourceUrl(), requestObject, IpPool.class, credential);
    }

    public IpPool update(IpPool ipPool) throws SendGridException {
        // TODO: this doesn't work right now because the name is the identifier, will
        // need to wait until SendGrid implements pool IDs.
        IpPool requestObject = new IpPool();
        requestObject.setName(ipPool.getName());
        return client.put(getResourceUrl(ipPool), requestObject, IpPool.class, credential);
    }

    public void delete(IpPool ipPool) throws SendGridException {
        client.delete(getResourceUrl(ipPool), credential);
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
