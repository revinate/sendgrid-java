package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.SendGridCollection;
import com.revinate.sendgrid.model.SendGridEntity;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

import java.util.List;

public abstract class CollectionResource<T extends SendGridEntity, U extends SendGridCollection<T>> extends SendGridResource {

    protected final Class<T> entityType;
    protected final Class<U> collectionType;

    public CollectionResource(String baseUrl, SendGridHttpClient client, Credential credential, Class<T> entityType, Class<U> collectionType) {
        super(baseUrl, client, credential);
        this.entityType = entityType;
        this.collectionType = collectionType;
    }

    public List<T> list() throws SendGridException {
        return client.get(baseUrl, collectionType, credential).getData();
    }

    public T create(T requestObject) throws SendGridException {
        return client.post(baseUrl, requestObject, entityType, credential);
    }
}
