package com.revinate.sendgrid;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.*;
import com.revinate.sendgrid.net.SendGridApiClient;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SendGrid {

    public static final String VERSION = "3.0.0";
    public static final String USER_AGENT = "sendgrid/" + VERSION + ";java";

    private static final String V2_API = "api";
    private static final String V2_API_2 = "apiv2";
    private static final String V3_API = "v3";

    private static final String MAIL_ENDPOINT = "mail.send.json";
    private static final String SUBUSERS_ENDPOINT = "subusers";
    private static final String IPS_ENDPOINT = "ips";
    private static final String IP_POOLS_ENDPOINT = "ips/pools";
    private static final String API_KEYS_ENDPOINT = "api_keys";

    private String username;
    private String password;
    private String url;
    private SendGridApiClient client;

    /**
     * Constructor for using a username and password
     *
     * @param username SendGrid username
     * @param password SendGrid password
     */
    public SendGrid(String username, String password) {
        this.username = username;
        this.password = password;
        this.url = "https://api.sendgrid.com";
        this.client = new SendGridApiClient(USER_AGENT);
    }

    /**
     * Constructor for using an API key
     *
     * @param apiKey SendGrid api key
     */
    public SendGrid(String apiKey) {
        this(null, apiKey);
    }

    public SendGrid setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getVersion() {
        return VERSION;
    }

    public SendGrid setClient(SendGridApiClient client) {
        this.client = client;
        return this;
    }

    public Response send(Email email) throws SendGridException {
        try {
            HttpResponse response = client.postV2(email.toHttpEntity(username, password),
                    getResourceUrl(V2_API, MAIL_ENDPOINT), username, password);
            return new Response(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<Subuser> getSubusers() throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, SUBUSERS_ENDPOINT), username, password);
            String content = EntityUtils.toString(response.getEntity());
            Subuser[] subusers = JsonUtils.fromJson(content, Subuser[].class);
            return Arrays.asList(subusers);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public Subuser getSubuser(String id) throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, SUBUSERS_ENDPOINT, id), username, password);
            String content = EntityUtils.toString(response.getEntity());
            return JsonUtils.fromJson(content, Subuser.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public Subuser createSubuser(Subuser subuser) throws SendGridException {
        try {
            HttpResponse response = client.post(subuser.toHttpEntity(),
                    getResourceUrl(V3_API, SUBUSERS_ENDPOINT), username, password);
            String content = EntityUtils.toString(response.getEntity());
            return JsonUtils.fromJson(content, Subuser.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public void deleteSubuser(String id) throws SendGridException {
        try {
            client.delete(getResourceUrl(V3_API, SUBUSERS_ENDPOINT, id), username, password);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<Ip> getIps() throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, IPS_ENDPOINT), username, password);
            String content = EntityUtils.toString(response.getEntity());
            Ip[] ips = JsonUtils.fromJson(content, Ip[].class);
            return Arrays.asList(ips);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public Ip getIp(String id) throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, IPS_ENDPOINT, id), username, password);
            String content = EntityUtils.toString(response.getEntity());
            return JsonUtils.fromJson(content, Ip.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<IpPool> getIpPools() throws SendGridException {
        return getIpPools(null);
    }

    public List<IpPool> getIpPools(String subuserName) throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, IP_POOLS_ENDPOINT),
                    username, password, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            IpPool[] ipPools = JsonUtils.fromJson(content, IpPool[].class);
            return Arrays.asList(ipPools);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public IpPool getIpPool(String id) throws SendGridException {
        return getIpPool(id, null);
    }

    public IpPool getIpPool(String id, String subuserName) throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, IP_POOLS_ENDPOINT, id),
                    username, password, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            return JsonUtils.fromJson(content, IpPool.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public IpPool createIpPool(IpPool ipPool) throws SendGridException {
        return createIpPool(ipPool, null);
    }

    public IpPool createIpPool(IpPool ipPool, String subuserName) throws SendGridException {
        try {
            HttpResponse response = client.post(ipPool.toHttpEntity(),
                    getResourceUrl(V3_API, IP_POOLS_ENDPOINT), username, password, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            return JsonUtils.fromJson(content, IpPool.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<ApiKey> getApiKeys() throws SendGridException {
        return getApiKeys(null);
    }

    public List<ApiKey> getApiKeys(String subuserName) throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, API_KEYS_ENDPOINT),
                    username, password, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            ApiKeysResponse apiKeysResponse = JsonUtils.fromJson(content, ApiKeysResponse.class);
            return apiKeysResponse.getResult();
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public ApiKey getApiKey(String id) throws SendGridException {
        return getApiKey(id, null);
    }

    public ApiKey getApiKey(String id, String subuserName) throws SendGridException {
        try {
            HttpResponse response = client.get(getResourceUrl(V3_API, API_KEYS_ENDPOINT, id),
                    username, password, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            return JsonUtils.fromJson(content, ApiKey.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public ApiKey createApiKey(ApiKey apiKey) throws SendGridException {
        return createApiKey(apiKey, null);
    }

    public ApiKey createApiKey(ApiKey apiKey, String subuserName) throws SendGridException {
        try {
            HttpResponse response = client.post(apiKey.toHttpEntity(),
                    getResourceUrl(V3_API, API_KEYS_ENDPOINT), username, password, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            return JsonUtils.fromJson(content, ApiKey.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public void deleteApiKey(String id) throws SendGridException {
        deleteApiKey(id, null);
    }

    public void deleteApiKey(String id, String subuserName) throws SendGridException {
        try {
            client.delete(getResourceUrl(V3_API, API_KEYS_ENDPOINT, id), username, password, subuserName);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    private String getResourceUrl(String api, String endpoint) {
        return getResourceUrl(api, endpoint, (String) null);
    }

    private String getResourceUrl(String api, String endpoint, Identifiable resource) {
        return getResourceUrl(api, endpoint, resource.getPathId());
    }

    private String getResourceUrl(String api, String endpoint, String id) {
        String resourceUrl = url + "/" + api + "/" + endpoint;
        if (id == null) {
            return resourceUrl;
        } else {
            return resourceUrl + "/" + id;
        }
    }
}
