package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.Subuser;
import com.revinate.sendgrid.model.SubuserCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class SubusersResource extends CollectionResource<Subuser, SubuserCollection> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "subusers";

    public SubusersResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, Subuser.class, SubuserCollection.class);
    }

    public SubuserResource entity(Subuser subuser) {
        return new SubuserResource(getUrl(), client, credential, subuser);
    }

    public SubuserResource entity(String id) {
        return new SubuserResource(getUrl(), client, credential, id);
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
