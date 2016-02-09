package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Subuser;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.Map;

public class SubuserResource extends EntityResource<Subuser> {

    public SubuserResource(String baseUrl, SendGridHttpClient client, Credential credential, Subuser subuser) {
        super(baseUrl, client, credential, Subuser.class, subuser);
    }

    public SubuserResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, Subuser.class, id);
    }

    @Override
    public Subuser update(Subuser subuser) throws SendGridException {
        // TODO: fix this URL, and do a better job of copying the subuser
        Subuser response = client.put(getUrl() + "/" + IpsResource.ENDPOINT,
                subuser.getIps(), Subuser.class, credential);
        response.setId(subuser.getId());
        response.setUsername(subuser.getUsername());
        response.setEmail(subuser.getEmail());
        response.setDisabled(subuser.getDisabled());
        return response;
    }

    @Override
    public Subuser partialUpdate(Map<String, Object> requestObject) throws SendGridException {
        // TODO: get the rest of the subuser
        client.patch(getUrl(), requestObject, credential);
        Subuser response = new Subuser();
        response.setDisabled((Boolean) requestObject.get("disabled"));
        return response;
    }
}
