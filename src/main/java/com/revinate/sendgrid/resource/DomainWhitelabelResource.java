package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.Whitelabel;
import com.revinate.sendgrid.model.WhitelabelValidation;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class DomainWhitelabelResource extends EntityResource<Whitelabel> {

    public DomainWhitelabelResource(String baseUrl, SendGridHttpClient client, Credential credential, Whitelabel whitelabel) {
        super(baseUrl, client, credential, Whitelabel.class, whitelabel);
    }

    public DomainWhitelabelResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, Whitelabel.class, id);
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
    public Whitelabel update(Whitelabel whitelabel) throws SendGridException {
        throw unsupported();
    }

    public WhitelabelValidation validate() throws SendGridException {
        return client.post(getUrl() + "/validate", WhitelabelValidation.class, credential);
    }
}
