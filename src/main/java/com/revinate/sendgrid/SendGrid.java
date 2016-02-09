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

    private SendGridApiClient v2Client;

    // TODO: change secondary constructors into builder
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
        this(LIVE_URL, new SendGridHttpClient(USER_AGENT, maxConnections), credential);
    }

    public SendGrid(String url, SendGridHttpClient client, String apiKey) {
        this(url, client, new ApiKeyCredential(apiKey));
    }

    public SendGrid(String url, SendGridHttpClient client, Credential credential) {
        super(url, client, credential);
        this.v2Client = new SendGridApiClient(USER_AGENT);
    }

    @Override
    public void close() {
        client.close();
    }

    public RootResource onBehalfOf(String username) {
        OnBehalfOfCredential onBehalfOfCredential = new OnBehalfOfCredential(credential, username);
        return new RootResource(url, client, onBehalfOfCredential);
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
}
