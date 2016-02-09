package com.revinate.sendgrid.net;

import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

    public HttpResponse postV2(HttpEntity entity, String url, Credential credential) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        if (credential != null && credential instanceof ApiKeyCredential) {
            httpPost.setHeader("Authorization", "Bearer " + ((ApiKeyCredential) credential).getApiKey());
        }
        return this.client.execute(httpPost);
    }
}
