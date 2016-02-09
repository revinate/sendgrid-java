package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.*;
import com.revinate.sendgrid.model.ApiError;
import com.revinate.sendgrid.model.ApiErrorsResponse;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
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

    public SendGridHttpClient(CloseableHttpClient client) {
        this(client, new StringResponseHandler());
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
        String responseBody = execute(HttpGet.METHOD_NAME, url, credential, null, null);
        return fromJson(responseBody, type);
    }

    public <T> T post(String url, Object requestObject, Class<T> type, Credential credential) throws SendGridException {
        String requestBody = toJson(requestObject);
        String responseBody = execute(HttpPost.METHOD_NAME, url, credential, requestBody, "application/json");
        return fromJson(responseBody, type);
    }

    public <T> T put(String url, Object requestObject, Class<T> type, Credential credential) throws SendGridException {
        String requestBody = toJson(requestObject);
        String responseBody = execute(HttpPut.METHOD_NAME, url, credential, requestBody, "application/json");
        return fromJson(responseBody, type);
    }

    public <T> T patch(String url, Object requestObject, Class<T> type, Credential credential) throws SendGridException {
        String requestBody = toJson(requestObject);
        String responseBody = execute(HttpPatch.METHOD_NAME, url, credential, requestBody, "application/json");
        return fromJson(responseBody, type);
    }

    public void patch(String url, Object requestObject, Credential credential) throws SendGridException {
        String requestBody = toJson(requestObject);
        execute(HttpPatch.METHOD_NAME, url, credential, requestBody, "application/json");
    }

    public void delete(String url, Credential credential) throws SendGridException {
        execute(HttpDelete.METHOD_NAME, url, credential, null, null);
    }

    private String execute(String method, String url, Credential credential,
                           String requestBody, String contentType) throws SendGridException {
        RequestBuilder builder = RequestBuilder.create(method).setUri(url);

        for (Header header : credential.toHttpHeaders()) {
            builder.setHeader(header);
        }

        if (requestBody != null) {
            builder.setEntity(EntityBuilder.create().setText(requestBody).build());
        }

        if (contentType != null) {
            builder.setHeader("Content-Type", "application/json");
        }

        try {
            return client.execute(builder.build(), responseHandler);
        } catch (HttpResponseException e) {
            throw handleResponseException(e);
        } catch (ClientProtocolException e) {
            throw new ApiConnectionException("HTTP protocol error while making API request to SendGrid", e);
        } catch (IOException e) {
            throw new ApiConnectionException("I/O error while making API request to SendGrid", e);
        }
    }

    private <T> T fromJson(String content, Class<T> type) throws ApiConnectionException {
        if (content == null) {
            throw new ApiConnectionException("Response contains no content");
        }

        try {
            return JsonUtils.fromJson(content, type);
        } catch (IOException e) {
            throw new ApiConnectionException("Error while mapping response", e);
        }
    }

    private String toJson(Object object) throws InvalidRequestException {
        if (object == null) {
            throw new InvalidRequestException("Request object is null");
        }

        try {
            return JsonUtils.toJson(object);
        } catch (IOException e) {
            throw new InvalidRequestException("Error while mapping request", e);
        }
    }

    private SendGridException handleResponseException(HttpResponseException e) {
        String responseBody = e.getMessage();
        String message;
        List<ApiError> errors = new ArrayList<ApiError>();
        try {
            ApiErrorsResponse apiErrorsResponse = JsonUtils.fromJson(responseBody, ApiErrorsResponse.class);
            message = apiErrorsResponse.toString();
            errors.addAll(apiErrorsResponse.getErrors());
        } catch (IOException ex) {
            message = responseBody;
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
