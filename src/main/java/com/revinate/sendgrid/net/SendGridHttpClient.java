package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.*;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

import static com.revinate.sendgrid.SendGrid.USER_AGENT;

public class SendGridHttpClient {

    private final HttpClient client;
    private final StringResponseHandler responseFactory;

    public SendGridHttpClient() {
        this(HttpClientBuilder.create().setUserAgent(USER_AGENT).build(), new StringResponseHandler());
    }

    public SendGridHttpClient(HttpClient client, StringResponseHandler responseFactory) {
        this.client = client;
        this.responseFactory = responseFactory;
    }

    public String get(String url, Credential credential) throws SendGridException {
        HttpGet request = new HttpGet(url);
        request.setHeaders(credential.toHttpHeaders());

        try {
            return client.execute(request, responseFactory);
        } catch (HttpResponseException e) {
            throw createException(e);
        } catch (IOException e) {
            throw new ApiConnectionException("IOException while making API request to SendGrid.", e);
        }
    }

    private SendGridException createException(HttpResponseException e) {
        String responseBody = e.getMessage();
        switch (e.getStatusCode()) {
            case 400:
                return new InvalidRequestException(responseBody);
            case 401:
                return new AuthenticationException(responseBody);
            case 404:
                return new NotFoundException(responseBody);
            default:
                return new ApiException(responseBody);
        }
    }
}
