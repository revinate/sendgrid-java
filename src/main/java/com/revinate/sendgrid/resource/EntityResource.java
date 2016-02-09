package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.SendGridEntity;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.Map;

public abstract class EntityResource<T extends SendGridEntity> extends SendGridResource {

    protected final Class<T> entityType;
    protected final String id;

    public EntityResource(String baseUrl, SendGridHttpClient client, Credential credential, Class<T> entityType, T entity) {
        this(baseUrl, client, credential, entityType, entity.getEntityId());
    }

    public EntityResource(String baseUrl, SendGridHttpClient client, Credential credential, Class<T> entityType, String id) {
        super(baseUrl, client, credential);
        this.entityType = entityType;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public T retrieve() throws SendGridException {
        return client.get(getUrl(), entityType, credential);
    }

    public T update(T entity) throws SendGridException {
        return client.put(getUrl(), entity, entityType, credential);
    }

    public T partialUpdate(Map<String, Object> requestObject) throws SendGridException {
        return client.patch(getUrl(), requestObject, entityType, credential);
    }

    public void delete() throws SendGridException {
        client.delete(getUrl(), credential);
    }

    protected String getUrl() throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException("Missing entity identifier");
        }
        return String.format("%s/%s", baseUrl, id);
    }
}
