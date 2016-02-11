package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.SendGridModel;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;

public abstract class SingularEntityResource<T extends SendGridModel> extends SendGridResource {

    protected final Class<T> entityType;

    public SingularEntityResource(String baseUrl, SendGridHttpClient client, Credential credential, Class<T> entityType) {
        super(baseUrl, client, credential);
        this.entityType = entityType;
    }

    public T retrieve() throws SendGridException {
        return client.get(getUrl(), entityType, credential);
    }

    public T create(T entity) throws SendGridException {
        return client.post(getUrl(), entityType, credential, entity, RequestType.JSON);
    }

    public T update(T entity) throws SendGridException {
        return client.put(getUrl(), entityType, credential, entity, RequestType.JSON);
    }

    public void delete() throws SendGridException {
        client.delete(getUrl(), credential);
    }

    protected String getUrl() {
        return String.format("%s/%s", baseUrl, getEndpoint());
    }

    protected abstract String getEndpoint();
}
