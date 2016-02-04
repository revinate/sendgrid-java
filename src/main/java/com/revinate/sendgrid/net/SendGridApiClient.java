package com.revinate.sendgrid.net;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class SendGridApiClient {

    private CloseableHttpClient client;

    public SendGridApiClient(String userAgent) {
        this.client = HttpClientBuilder.create().setUserAgent(userAgent).build();
    }

    public SendGridApiClient setClient(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    public HttpResponse get(String url, String username, String password) throws IOException {
        return get(url, username, password, null);
    }

    public HttpResponse get(String url, String username, String password, String subuserName) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", getAuthHeaderValue(username, password));
        if (subuserName != null) {
            httpGet.setHeader("On-Behalf-Of", subuserName);
        }
        return this.client.execute(httpGet);
    }

    public HttpResponse post(HttpEntity entity, String url, String username, String password) throws IOException {
        return post(entity, url, username, password, null);
    }

    public HttpResponse post(HttpEntity entity, String url, String username, String password, String subuserName) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        httpPost.setHeader("Authorization", getAuthHeaderValue(username, password));
        if (subuserName != null) {
            httpPost.setHeader("On-Behalf-Of", subuserName);
        }
        return this.client.execute(httpPost);
    }

    public HttpResponse delete(String url, String username, String password) throws IOException {
        return delete(url, username, password, null);
    }

    public HttpResponse delete(String url, String username, String password, String subuserName) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeader("Authorization", getAuthHeaderValue(username, password));
        if (subuserName != null) {
            httpDelete.setHeader("On-Behalf-Of", subuserName);
        }
        return this.client.execute(httpDelete);
    }

    private String getAuthHeaderValue(String username, String password) {
        if (username == null) {
            return "Bearer " + password;
        } else {
            return "Basic " +  Base64.encodeBase64String(
                    (username + ":" + password).getBytes());
        }
    }
}
