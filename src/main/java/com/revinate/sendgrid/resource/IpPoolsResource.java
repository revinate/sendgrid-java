package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.IpPool;
import com.revinate.sendgrid.model.IpPoolCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class IpPoolsResource extends CollectionResource<IpPool, IpPoolCollection> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "ips/pools";

    public IpPoolsResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, IpPool.class, IpPoolCollection.class);
    }

    public IpPoolResource entity(IpPool ipPool) {
        return new IpPoolResource(getUrl(), client, credential, ipPool);
    }

    public IpPoolResource entity(String id) {
        return new IpPoolResource(getUrl(), client, credential, id);
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
