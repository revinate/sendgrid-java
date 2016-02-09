package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Subuser;
import com.revinate.sendgrid.model.SubuserCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;
import java.util.Map;

public class SubuserResource extends SendGridResource {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "subusers";

    public SubuserResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public List<Subuser> list() throws SendGridException {
        return client.get(baseUrl, SubuserCollection.class, credential).getData();
    }

    public Subuser retrieve(String id) throws SendGridException {
        return client.get(getEntityUrl(id), Subuser.class, credential);
    }

    public Subuser create(Subuser requestObject) throws SendGridException {
        return client.post(baseUrl, requestObject, Subuser.class, credential);
    }

    public Subuser update(Subuser subuser) throws SendGridException {
        Subuser response = client.put(getEntityUrl(subuser) + "/" + IpResource.ENDPOINT,
                subuser.getIps(), Subuser.class, credential);
        response.setId(subuser.getId());
        response.setUsername(subuser.getUsername());
        response.setEmail(subuser.getEmail());
        response.setDisabled(subuser.getDisabled());
        return response;
    }

    public Subuser partialUpdate(Subuser subuser, Map<String, Object> requestObject) throws SendGridException {
        client.patch(getEntityUrl(subuser), requestObject, credential);
        Subuser response = new Subuser();
        response.setId(subuser.getId());
        response.setUsername(subuser.getUsername());
        response.setEmail(subuser.getEmail());
        response.setDisabled((Boolean) requestObject.get("disabled"));
        return response;
    }

    public void delete(Subuser subuser) throws SendGridException {
        client.delete(getEntityUrl(subuser), credential);
    }
}
