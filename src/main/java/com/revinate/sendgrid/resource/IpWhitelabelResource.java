package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.IpWhitelabel;
import com.revinate.sendgrid.model.WhitelabelValidation;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class IpWhitelabelResource extends EntityResource<IpWhitelabel> {

    public IpWhitelabelResource(String baseUrl, SendGridHttpClient client, Credential credential, IpWhitelabel ipWhitelabel) {
        super(baseUrl, client, credential, IpWhitelabel.class, ipWhitelabel);
    }

    public IpWhitelabelResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, IpWhitelabel.class, id);
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
    public IpWhitelabel update(IpWhitelabel ipWhitelabel) throws SendGridException {
        throw unsupported();
    }

    public WhitelabelValidation validate() throws SendGridException {
        return client.post(getUrl() + "/validate", WhitelabelValidation.class, credential);
    }
}
