package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.IpPool;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.Map;

public class IpPoolResource extends EntityResource<IpPool> {

    public IpPoolResource(String baseUrl, SendGridHttpClient client, Credential credential, IpPool ipPool) {
        super(baseUrl, client, credential, IpPool.class, ipPool);
    }

    public IpPoolResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, IpPool.class, id);
    }

    @Override
    public IpPool update(IpPool ipPool) throws SendGridException {
        IpPool requestObject = new IpPool();
        requestObject.setName(ipPool.getName());
        return client.put(getUrl(), requestObject, IpPool.class, credential);
    }

    @Override
    public IpPool partialUpdate(Map<String, Object> requestObject) throws SendGridException {
        throw unsupported();
    }
}
