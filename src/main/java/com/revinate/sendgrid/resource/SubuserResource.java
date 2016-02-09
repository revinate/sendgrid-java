package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Subuser;
import com.revinate.sendgrid.model.SubuserCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;
import java.util.Map;

public class SubuserResource extends SendGridResource {

    private static final ApiVersion API_VERSION = ApiVersion.V3;
    private static final String ENDPOINT = "subusers";
    private static final String IPS_ENDPOINT = "ips";

    public SubuserResource(String url, SendGridHttpClient client, Credential credential) {
        super(url, client, credential);
    }

    public List<Subuser> list() throws SendGridException {
        return client.get(getResourceUrl(), SubuserCollection.class, credential).getData();
    }

    public Subuser retrieve(String id) throws SendGridException {
        return client.get(getResourceUrl(id), Subuser.class, credential);
    }

    public Subuser create(Subuser requestObject) throws SendGridException {
        return client.post(getResourceUrl(), requestObject, Subuser.class, credential);
    }

    public Subuser update(Subuser subuser) throws SendGridException {
        Subuser response = client.put(getResourceUrl(subuser, IPS_ENDPOINT), subuser.getIps(), Subuser.class, credential);
        response.setId(subuser.getId());
        response.setUsername(subuser.getUsername());
        response.setEmail(subuser.getEmail());
        response.setDisabled(subuser.getDisabled());
        return response;
    }

    public Subuser partialUpdate(Subuser subuser, Map<String, Object> requestObject) throws SendGridException {
        client.patch(getResourceUrl(subuser), requestObject, credential);
        Subuser response = new Subuser();
        response.setId(subuser.getId());
        response.setUsername(subuser.getUsername());
        response.setEmail(subuser.getEmail());
        response.setDisabled((Boolean) requestObject.get("disabled"));
        return response;
    }

    public void delete(Subuser subuser) throws SendGridException {
        client.delete(getResourceUrl(subuser), credential);
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
