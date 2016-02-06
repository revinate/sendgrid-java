package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.*;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;

public class SendGridHttpClient implements Closeable {

    private final CloseableHttpClient client;
    private final ResponseHandler<String> responseHandler;

    public SendGridHttpClient(String userAgent, int maxConnections) {
        this(HttpClients.custom()
                .setUserAgent(userAgent)
                .setMaxConnTotal(maxConnections)
                .setMaxConnPerRoute(maxConnections)
                .build(),
                new StringResponseHandler());
    }

    public SendGridHttpClient(CloseableHttpClient client, ResponseHandler<String> responseHandler) {
        this.client = client;
        this.responseHandler = responseHandler;
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            // do nothing
        }
    }

    public String get(String url, Credential credential) throws SendGridException {
        HttpGet request = new HttpGet(url);
        request.setHeaders(credential.toHttpHeaders());

        try {
            return client.execute(request, responseHandler);
        } catch (HttpResponseException e) {
            throw createException(e);
        } catch (ClientProtocolException e) {
            throw new ApiConnectionException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ApiConnectionException("IOException while making API request to SendGrid.", e);
        }
    }

    private SendGridException createException(HttpResponseException e) {
        String message = e.getMessage();
        switch (e.getStatusCode()) {
            case 400:
                return new InvalidRequestException(message);
            case 401:
                return new AuthenticationException(message);
            case 404:
                return new ResourceNotFoundException(message);
            default:
                return new ApiException(message);
        }
    }
}
