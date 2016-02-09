package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.Subuser;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class RootResource extends SendGridResource {

    public RootResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public ApiKeysResource apiKeys() {
        return new ApiKeysResource(getApiUrl(ApiKeysResource.API_VERSION), client, credential);
    }

    public ApiKeyResource apiKey(ApiKey apiKey) {
        return apiKeys().entity(apiKey);
    }

    public ApiKeyResource apiKey(String id) {
        return apiKeys().entity(id);
    }

    public IpsResource ips() {
        return new IpsResource(getApiUrl(IpsResource.API_VERSION), client, credential);
    }

    public IpResource ip(Ip ip) {
        return ips().entity(ip);
    }

    public IpResource ip(String id) {
        return ips().entity(id);
    }

    public IpPoolResource ipPools() {
        return new IpPoolResource(getCollectionUrl(IpPoolResource.API_VERSION,
                IpPoolResource.ENDPOINT), client, credential);
    }

    public SubusersResource subusers() {
        return new SubusersResource(getApiUrl(SubusersResource.API_VERSION), client, credential);
    }

    public SubuserResource subuser(Subuser subuser) {
        return subusers().entity(subuser);
    }

    public SubuserResource subuser(String id) {
        return subusers().entity(id);
    }
}
