package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public abstract class SingularEntityResource<T> extends SendGridResource {

    protected final Class<T> entityType;

    public SingularEntityResource(String baseUrl, SendGridHttpClient client, Credential credential, Class<T> entityType) {
        super(baseUrl, client, credential);
        this.entityType = entityType;
    }

    public T retrieve() throws SendGridException {
        return client.get(getUrl(), entityType, credential);
    }

    public T create(T requestObject) throws SendGridException {
        return client.post(getUrl(), requestObject, entityType, credential);
    }

    public T update(T entity) throws SendGridException {
        return client.put(getUrl(), entity, entityType, credential);
    }

    public void delete() throws SendGridException {
        client.delete(getUrl(), credential);
    }

    protected String getUrl() {
        return String.format("%s/%s", baseUrl, getEndpoint());
    }

    protected abstract String getEndpoint();
}
