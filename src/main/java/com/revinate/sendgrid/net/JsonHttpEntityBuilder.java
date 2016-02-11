package com.revinate.sendgrid.net;

import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonHttpEntityBuilder implements HttpEntityBuilder {

    private Object content;

    public static JsonHttpEntityBuilder create() {
        return new JsonHttpEntityBuilder();
    }

    @Override
    public JsonHttpEntityBuilder setContent(Email email) {
        this.content = email;
        return this;
    }

    @Override
    public JsonHttpEntityBuilder setContent(Object content) {
        this.content = content;
        return this;
    }

    @Override
    public HttpEntity build() throws IOException {
        if (content == null) {
            throw new IOException("Content is null");
        }
        return EntityBuilder.create().setText(JsonUtils.toJson(content)).build();
    }

    @Override
    public List<Header> getHeaders() {
        return Collections.singletonList((Header) new BasicHeader("Content-Type", "application/json"));
    }
}
