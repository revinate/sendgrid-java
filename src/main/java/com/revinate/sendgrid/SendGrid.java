package com.revinate.sendgrid;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.model.Response;
import com.revinate.sendgrid.net.SendGridApiClient;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.net.auth.OnBehalfOfCredential;
import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import com.revinate.sendgrid.resource.RootResource;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;

public final class SendGrid extends RootResource implements Closeable {

    public static final String VERSION = "3.0.0";
    public static final String USER_AGENT = "sendgrid/" + VERSION + ";java";
    public static final String LIVE_URL = "https://api.sendgrid.com";
    public static final int DEFAULT_MAX_CONNECTIONS = 20;

    private final Credential credential;
    private String url;
    private SendGridHttpClient client;
    private SendGridApiClient v2Client;

    public SendGrid(String username, String password) {
        this(username, password, DEFAULT_MAX_CONNECTIONS);
    }

    public SendGrid(String username, String password, int maxConnections) {
        this(new UsernamePasswordCredential(username, password), maxConnections);
    }

    public SendGrid(String apiKey) {
        this(apiKey, DEFAULT_MAX_CONNECTIONS);
    }

    public SendGrid(String apiKey, int maxConnections) {
        this(new ApiKeyCredential(apiKey), maxConnections);
    }

    public SendGrid(Credential credential) {
        this(credential, DEFAULT_MAX_CONNECTIONS);
    }

    public SendGrid(Credential credential, int maxConnections) {
        this.credential = credential;
        this.url = LIVE_URL;
        this.client = new SendGridHttpClient(USER_AGENT, maxConnections);
        this.v2Client = new SendGridApiClient(USER_AGENT);
    }

    @Override
    public Credential getCredential() {
        return credential;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public SendGrid setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public SendGridHttpClient getClient() {
        return client;
    }

    public SendGrid setClient(SendGridHttpClient client) {
        if (this.client != client) {
            this.client.close();
            this.client = client;
        }
        return this;
    }

    @Override
    public void close() {
        client.close();
    }

    public RootResource onBehalfOf(String username) {
        OnBehalfOfCredential onBehalfOfCredential = new OnBehalfOfCredential(credential, username);
        return new WithCredentialOverlay(onBehalfOfCredential);
    }

    public Response send(Email email) throws SendGridException {
        try {
            HttpResponse response = v2Client.postV2(email.toHttpEntity(credential),
                    url + "/api/mail.send.json", credential);
            return new Response(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new SendGridException(e);
        }
    }

    private class WithCredentialOverlay extends RootResource {

        private final Credential credential;

        public WithCredentialOverlay(Credential credential) {
            this.credential = credential;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public SendGridHttpClient getClient() {
            return client;
        }

        @Override
        public Credential getCredential() {
            return credential;
        }
    }
}
