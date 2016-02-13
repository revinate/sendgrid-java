package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Monitor;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.Map;

public class MonitorResource extends SingularEntityResource<Monitor> {

    public static final String ENDPOINT = "monitor";

    public MonitorResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, Monitor.class);
    }

    @Override
    public Monitor partialUpdate(Map<String, Object> requestObject) throws SendGridException {
        throw unsupported();
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
