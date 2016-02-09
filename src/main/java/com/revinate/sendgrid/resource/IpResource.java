package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.Map;

public class IpResource extends EntityResource<Ip> {

    public IpResource(String baseUrl, SendGridHttpClient client, Credential credential, Ip ip) {
        super(baseUrl, client, credential, Ip.class, ip);
    }

    public IpResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, Ip.class, id);
    }

    @Override
    public Ip update (Ip ip) throws SendGridException {
        throw unsupported();
    }

    @Override
    public Ip partialUpdate(Map<String, Object> requestObject) throws SendGridException {
        throw unsupported();
    }

    @Override
    public void delete() throws SendGridException {
        throw unsupported();
    }
}
