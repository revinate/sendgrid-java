package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.HttpException;
import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

import static com.revinate.sendgrid.SendGrid.USER_AGENT;

public class SendGridHttpClient {

    private final HttpClient client;
    private final SendGridResponseFactory responseFactory;

    public SendGridHttpClient() {
        this(HttpClientBuilder.create().setUserAgent(USER_AGENT).build(), new SendGridResponseFactory());
    }

    public SendGridHttpClient(HttpClient client, SendGridResponseFactory responseFactory) {
        this.client = client;
        this.responseFactory = responseFactory;
    }

    public SendGridResponse get(String url, Credential credential) throws SendGridException {
        HttpGet request = new HttpGet(url);
        request.setHeaders(credential.toHttpHeaders());

        try {
            HttpResponse response = client.execute(request);
            return responseFactory.create(response);
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }
}
