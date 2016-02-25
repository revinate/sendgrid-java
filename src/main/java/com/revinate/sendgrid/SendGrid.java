package com.revinate.sendgrid;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.net.auth.OnBehalfOfCredential;
import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import com.revinate.sendgrid.resource.RootResource;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.Closeable;

public final class SendGrid extends RootResource implements Closeable {

    public static final String VERSION = "3.0.3";
    public static final String USER_AGENT = "revinate-sendgrid/" + VERSION + ";java";
    public static final String LIVE_URL = "https://api.sendgrid.com";
    public static final int DEFAULT_MAX_CONNECTIONS = 20;

    public SendGrid(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    @Override
    public void close() {
        client.close();
    }

    public RootResource onBehalfOf(String username) {
        OnBehalfOfCredential onBehalfOfCredential = new OnBehalfOfCredential(credential, username);
        return new RootResource(baseUrl, client, onBehalfOfCredential);
    }

    public RootResource onBehalfOf(Credential credentialOverride) {
        return new RootResource(baseUrl, client, credentialOverride);
    }

    public static Builder create(String username, String password) {
        return new Builder(username, password);
    }

    public static Builder create(String apiKey) {
        return new Builder(apiKey);
    }

    public static Builder create(Credential credential) {
        return new Builder(credential);
    }

    public static class Builder {

        private String baseUrl = LIVE_URL;
        private int maxConnections = DEFAULT_MAX_CONNECTIONS;
        private CloseableHttpClient httpClient;
        private SendGridHttpClient client;
        private final Credential credential;

        public Builder(String username, String password) {
            this.credential = new UsernamePasswordCredential(username, password);
        }

        public Builder(String apiKey) {
            this.credential = new ApiKeyCredential(apiKey);
        }

        public Builder(Credential credential) {
            this.credential = credential;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setClient(SendGridHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public Builder setHttpClient(CloseableHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public SendGrid build() {
            SendGridHttpClient finalClient = client;
            if (finalClient == null) {
                if (httpClient != null) {
                    finalClient = new SendGridHttpClient(httpClient);
                } else {
                    finalClient = new SendGridHttpClient(USER_AGENT, maxConnections);
                }
            }
            return new SendGrid(baseUrl, finalClient, credential);
        }
    }
}
