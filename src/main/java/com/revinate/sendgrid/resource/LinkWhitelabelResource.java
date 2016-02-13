package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Whitelabel;
import com.revinate.sendgrid.model.WhitelabelValidation;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class LinkWhitelabelResource extends EntityResource<Whitelabel> {

    public LinkWhitelabelResource(String baseUrl, SendGridHttpClient client, Credential credential, Whitelabel whitelabel) {
        super(baseUrl, client, credential, Whitelabel.class, whitelabel);
    }

    public LinkWhitelabelResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, Whitelabel.class, id);
    }

    @Override
    public Whitelabel update(Whitelabel whitelabel) throws SendGridException {
        throw unsupported();
    }

    public WhitelabelValidation validate() throws SendGridException {
        return client.post(getUrl() + "/validate", WhitelabelValidation.class, credential);
    }
}
