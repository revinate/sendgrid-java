package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.*;
import com.revinate.sendgrid.model.ApiError;
import com.revinate.sendgrid.model.ApiErrorsResponse;
import com.revinate.sendgrid.model.Response;
import com.revinate.sendgrid.model.SendGridModel;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendGridHttpClient implements Closeable {

    public enum RequestType {
        JSON, MULTIPART
    }

    private final CloseableHttpClient client;
    private final ResponseHandler<String> responseHandler;

    public SendGridHttpClient(String userAgent, int maxConnections) {
        this(HttpClients.custom()
                .setUserAgent(userAgent)
                .setMaxConnTotal(maxConnections)
                .setMaxConnPerRoute(maxConnections)
                .build());
    }

    public SendGridHttpClient(CloseableHttpClient client) {
        this(client, new StringResponseHandler());
    }

    SendGridHttpClient(CloseableHttpClient client, ResponseHandler<String> responseHandler) {
        this.client = client;
        this.responseHandler = responseHandler;
    }

    public CloseableHttpClient getClient() {
        return client;
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
        String responseBody = execute(HttpGet.METHOD_NAME, url, credential, null);
        return fromJson(responseBody, type);
    }

    public <T> T post(String url, Class<T> type, Credential credential, SendGridModel requestObject,
                      RequestType requestType) throws SendGridException {
        HttpEntityBuilder builder = httpEntityBuilder(requestObject, requestType, credential);
        String responseBody = execute(HttpPost.METHOD_NAME, url, credential, builder);
        return fromJson(responseBody, type);
    }

    public <T> T put(String url, Class<T> type, Credential credential, SendGridModel requestObject,
                     RequestType requestType) throws SendGridException {
        HttpEntityBuilder builder = httpEntityBuilder(requestObject, requestType, credential);
        String responseBody = execute(HttpPut.METHOD_NAME, url, credential, builder);
        return fromJson(responseBody, type);
    }

    public <T> T put(String url, Class<T> type, Credential credential, List<String> requestObject,
                     RequestType requestType) throws SendGridException {
        HttpEntityBuilder builder = HttpEntityBuilder.create(requestType).setCredential(credential).setList(requestObject);
        String responseBody = execute(HttpPut.METHOD_NAME, url, credential, builder);
        return fromJson(responseBody, type);
    }

    public <T> T patch(String url, Class<T> type, Credential credential, Map<String, Object> requestObject,
                       RequestType requestType) throws SendGridException {
        HttpEntityBuilder builder = HttpEntityBuilder.create(requestType).setCredential(credential).setMap(requestObject);;
        String responseBody = execute(HttpPatch.METHOD_NAME, url, credential, builder);
        return fromJson(responseBody, type);
    }

    public void patch(String url, Credential credential, Map<String, Object> requestObject,
                      RequestType requestType) throws SendGridException {
        HttpEntityBuilder builder = HttpEntityBuilder.create(requestType).setCredential(credential).setMap(requestObject);;
        execute(HttpPatch.METHOD_NAME, url, credential, builder);
    }

    public void delete(String url, Credential credential) throws SendGridException {
        execute(HttpDelete.METHOD_NAME, url, credential, null);
    }

    private HttpEntityBuilder httpEntityBuilder(SendGridModel requestObject, RequestType requestType, Credential credential) {
        HttpEntityBuilder builder = HttpEntityBuilder.create(requestType).setCredential(credential);
        if (requestObject != null) {
            requestObject.accept(builder);
        }
        return builder;
    }

    private String execute(String method, String url, Credential credential, HttpEntityBuilder entityBuilder) throws SendGridException {
        RequestBuilder builder = RequestBuilder.create(method).setUri(url);

        for (Header header : credential.toHttpHeaders()) {
            builder.setHeader(header);
        }

        if (entityBuilder != null) {
            try {
                builder.setEntity(entityBuilder.build());
            } catch (IOException e) {
                throw new InvalidRequestException("Error while creating request entity", e);
            }

            for (Header header: entityBuilder.getHeaders()) {
                builder.setHeader(header);
            }
        }

        HttpUriRequest request = builder.build();

        try {
            return client.execute(request, responseHandler);
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

    private SendGridException handleResponseException(HttpResponseException e) {
        String responseBody = e.getMessage();
        String message;
        List<ApiError> errors = new ArrayList<ApiError>();
        try {
            ApiErrorsResponse apiErrorsResponse = JsonUtils.fromJson(responseBody, ApiErrorsResponse.class);
            message = apiErrorsResponse.toString();
            errors.addAll(apiErrorsResponse.getErrors());
        } catch (IOException e2) {
            try {
                Response response = JsonUtils.fromJson(responseBody, Response.class);
                message = response.toString();
                for (String error : response.getErrors()) {
                    errors.add(new ApiError(error));
                }
            } catch (IOException e3) {
                message = responseBody;
            }
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
