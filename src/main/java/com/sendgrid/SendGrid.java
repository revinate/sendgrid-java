package com.sendgrid;

import com.sendgrid.exception.SendGridException;
import com.sendgrid.model.Email;
import com.sendgrid.model.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SendGrid {

    private static final String VERSION = "2.2.2";
    private static final String USER_AGENT = "sendgrid/" + VERSION + ";java";

    private static final String MAIL_ENDPOINT = "/api/mail.send.json";

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
        HttpPost httppost = new HttpPost(this.url + MAIL_ENDPOINT);
        httppost.setEntity(email.toHttpEntity(this.username, this.password));

        // Using an API key
        if (this.username == null) {
            httppost.setHeader("Authorization", "Bearer " + this.password);
        }

        try {
            HttpResponse res = this.client.execute(httppost);
            return new Response(res.getStatusLine().getStatusCode(), EntityUtils.toString(res.getEntity()));
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }
}
