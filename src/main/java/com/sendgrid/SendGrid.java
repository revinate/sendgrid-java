package com.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sendgrid.exception.SendGridException;
import com.sendgrid.model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class SendGrid {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final String VERSION = "3.0.0";
    private static final String USER_AGENT = "sendgrid/" + VERSION + ";java";

    private static final String MAIL_ENDPOINT = "/api/mail.send.json";
    private static final String SUBUSERS_ENDPOINT = "/v3/subusers";
    private static final String IPS_ENDPOINT = "/v3/ips";
    private static final String IP_POOLS_ENDPOINT = "/v3/ips/pools";

    private String username;
    private String password;
    private String url;
    private CloseableHttpClient client;

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
        this.client = HttpClientBuilder.create().setUserAgent(USER_AGENT).build();
    }

    /**
     * Constructor for using an API key
     *
     * @param apiKey SendGrid api key
     */
    public SendGrid(String apiKey) {
        this.password = apiKey;
        this.username = null;
        this.url = "https://api.sendgrid.com";
        this.client = HttpClientBuilder.create().setUserAgent(USER_AGENT).build();
    }

    public SendGrid setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getVersion() {
        return VERSION;
    }

    public SendGrid setClient(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    public Response send(Email email) throws SendGridException {
        try {
            HttpResponse response = post(email.toHttpEntity(this.username, this.password), MAIL_ENDPOINT);
            return new Response(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<Subuser> getSubusers() throws SendGridException {
        try {
            HttpResponse response = get(SUBUSERS_ENDPOINT);
            String content = EntityUtils.toString(response.getEntity());
            Subuser[] subusers = OBJECT_MAPPER.readValue(content, Subuser[].class);
            return Arrays.asList(subusers);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public Subuser getSubuser(String id) throws SendGridException {
        try {
            HttpResponse response = get(SUBUSERS_ENDPOINT + "/" + id);
            String content = EntityUtils.toString(response.getEntity());
            return OBJECT_MAPPER.readValue(content, Subuser.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public Subuser createSubuser(Subuser subuser) throws SendGridException {
        try {
            HttpResponse response = post(subuser.toHttpEntity(), SUBUSERS_ENDPOINT);
            String content = EntityUtils.toString(response.getEntity());
            return OBJECT_MAPPER.readValue(content, Subuser.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public void deleteSubuser(String id) throws SendGridException {
        try {
            delete(SUBUSERS_ENDPOINT + "/" + id);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<Ip> getIps() throws SendGridException {
        try {
            HttpResponse response = get(IPS_ENDPOINT);
            String content = EntityUtils.toString(response.getEntity());
            Ip[] ips = OBJECT_MAPPER.readValue(content, Ip[].class);
            return Arrays.asList(ips);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public Ip getIp(String id) throws SendGridException {
        try {
            HttpResponse response = get(IPS_ENDPOINT + "/" + id);
            String content = EntityUtils.toString(response.getEntity());
            return OBJECT_MAPPER.readValue(content, Ip.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<IpPool> getIpPools() throws SendGridException {
        return getIpPools(null);
    }

    public List<IpPool> getIpPools(String subuserName) throws SendGridException {
        try {
            HttpResponse response = get(IP_POOLS_ENDPOINT, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            IpPool[] ipPools = OBJECT_MAPPER.readValue(content, IpPool[].class);
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
            HttpResponse response = get(IP_POOLS_ENDPOINT + "/" + id, subuserName);
            String content = EntityUtils.toString(response.getEntity());
            return OBJECT_MAPPER.readValue(content, IpPool.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public IpPool createIpPool(IpPool ipPool) throws SendGridException {
        try {
            HttpResponse response = post(ipPool.toHttpEntity(), SUBUSERS_ENDPOINT);
            String content = EntityUtils.toString(response.getEntity());
            return OBJECT_MAPPER.readValue(content, IpPool.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    private HttpResponse get(String endpoint) throws IOException {
        return get(endpoint, null);
    }

    private HttpResponse get(String endpoint, String subuserName) throws IOException {
        HttpGet httpGet = new HttpGet(this.url + endpoint);
        httpGet.setHeader("Authorization", getAuthHeaderValue());
        if (subuserName != null) {
            httpGet.setHeader("On-Behalf-Of", subuserName);
        }
        return this.client.execute(httpGet);
    }

    private HttpResponse post(HttpEntity entity, String endpoint) throws IOException {
        return post(entity, endpoint, null);
    }

    private HttpResponse post(HttpEntity entity, String endpoint, String subuserName) throws IOException {
        HttpPost httpPost = new HttpPost(this.url + endpoint);
        httpPost.setEntity(entity);
        httpPost.setHeader("Authorization", getAuthHeaderValue());
        if (subuserName != null) {
            httpPost.setHeader("On-Behalf-Of", subuserName);
        }
        return this.client.execute(httpPost);
    }

    private HttpResponse delete(String endpoint) throws IOException {
        return delete(endpoint, null);
    }

    private HttpResponse delete(String endpoint, String subuserName) throws IOException {
        HttpDelete httpDelete = new HttpDelete(this.url + endpoint);
        httpDelete.setHeader("Authorization", getAuthHeaderValue());
        if (subuserName != null) {
            httpDelete.setHeader("On-Behalf-Of", subuserName);
        }
        return this.client.execute(httpDelete);
    }

    private String getAuthHeaderValue() {
        if (this.username == null) {
            return "Bearer " + this.password;
        } else {
            return "Basic " +  Base64.getEncoder().encodeToString(
                    (this.username + ":" + this.password).getBytes());
        }
    }
}
