package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.*;
import com.revinate.sendgrid.model.ApiError;
import com.revinate.sendgrid.model.ApiErrorsResponse;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

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

    public <T> T get(String url, Class<T> type, Credential credential) throws SendGridException {
        HttpGet request = new HttpGet(url);
        request.setHeaders(credential.toHttpHeaders());
        return map(request(request), type);
    }

    public <T> T post(String url, Object requestObject, Class<T> type, Credential credential) throws SendGridException {
        String requestBody;
        try {
            requestBody = JsonUtils.toJson(requestObject);
        } catch (IOException e) {
            throw new SendGridException("IOException while mapping request", e);
        }

        HttpPost request = new HttpPost(url);
        request.setEntity(EntityBuilder.create().setText(requestBody).build());
        request.setHeaders(credential.toHttpHeaders());
        request.addHeader("Content-Type", "application/json");

        return map(request(request), type);
    }

    public void delete(String url, Credential credential) throws SendGridException {
        HttpDelete request = new HttpDelete(url);
        request.setHeaders(credential.toHttpHeaders());
        request(request);
    }

    private String request(HttpUriRequest request) throws SendGridException {
        try {
            return client.execute(request, responseHandler);
        } catch (HttpResponseException e) {
            throw handleResponseException(e);
        } catch (ClientProtocolException e) {
            throw new ApiConnectionException("HTTP protocol error while making API request to SendGrid", e);
        } catch (IOException e) {
            throw new ApiConnectionException("IOException while making API request to SendGrid", e);
        }
    }

    private <T> T map(String content, Class<T> type) throws SendGridException {
        if (content == null) {
            throw new SendGridException("Response contains no content");
        }

        try {
            return JsonUtils.fromJson(content, type);
        } catch (IOException e) {
            throw new SendGridException("IOException while mapping response", e);
        }
    }

    private SendGridException handleResponseException(HttpResponseException e) {
        String responseBody = e.getMessage();
        String message;
        List<ApiError> errors;
        try {
            ApiErrorsResponse apiErrorsResponse = JsonUtils.fromJson(responseBody, ApiErrorsResponse.class);
            message = apiErrorsResponse.toString();
            errors = apiErrorsResponse.getErrors();
        } catch (IOException ex) {
            message = responseBody;
            errors = null;
        }

        switch (e.getStatusCode()) {
            case 400:
                return new InvalidRequestException(message, errors);
            case 401:
                return new AuthenticationException(message, errors);
            case 404:
                return new ResourceNotFoundException(message, errors);
            default:
                return new ApiException(message, errors);
        }
    }
}
