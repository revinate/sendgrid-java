package com.sendgrid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sendgrid.exception.SendGridException;
import com.sendgrid.model.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

    public Subuser createSubuser(Subuser subuser) throws SendGridException {
        try {
            HttpEntity entity = post(subuser.toHttpEntity(), SUBUSERS_ENDPOINT).getEntity();
            return OBJECT_MAPPER.readValue(EntityUtils.toString(entity), Subuser.class);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<Ip> getIps() throws SendGridException {
        try {
            HttpEntity entity = get(IPS_ENDPOINT).getEntity();
            Ip[] ips = OBJECT_MAPPER.readValue(EntityUtils.toString(entity), Ip[].class);
            return Arrays.asList(ips);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    public List<IpPool> getIpPools() throws SendGridException {
        try {
            HttpEntity entity = get(IP_POOLS_ENDPOINT).getEntity();
            IpPool[] ipPools = OBJECT_MAPPER.readValue(EntityUtils.toString(entity), IpPool[].class);
            return Arrays.asList(ipPools);
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    private HttpResponse get(String endpoint) throws IOException {
        HttpGet httpGet = new HttpGet(this.url + endpoint);
        httpGet.setHeader("Authorization", getAuthHeaderValue());
        return this.client.execute(httpGet);
    }

    private HttpResponse post(HttpEntity entity, String endpoint) throws IOException {
        HttpPost httpPost = new HttpPost(this.url + endpoint);
        httpPost.setEntity(entity);
        httpPost.setHeader("Authorization", getAuthHeaderValue());
        return this.client.execute(httpPost);
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
