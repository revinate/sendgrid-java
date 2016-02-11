package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.IpPool;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.Map;

public class IpPoolResource extends EntityResource<IpPool> {

    public IpPoolResource(String baseUrl, SendGridHttpClient client, Credential credential, IpPool ipPool) {
        super(baseUrl, client, credential, IpPool.class, ipPool);
    }

    public IpPoolResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, IpPool.class, id);
    }

    public IpsResource ips() throws InvalidRequestException {
        return new IpsResource(getUrl(), client, credential);
    }

    public IpResource ip(Ip ip) throws InvalidRequestException {
        return ips().entity(ip);
    }

    public IpResource ip(String id) throws InvalidRequestException {
        return ips().entity(id);
    }

    @Override
    public IpPool update(IpPool ipPool) throws SendGridException {
        IpPool requestObject = new IpPool();
        requestObject.setName(ipPool.getName());
        return client.put(getUrl(), IpPool.class, credential, requestObject, RequestType.JSON);
    }

    @Override
    public IpPool partialUpdate(Map<String, Object> requestObject) throws SendGridException {
        throw unsupported();
    }
}
