package com.revinate.sendgrid.net;

import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonHttpEntityBuilder extends HttpEntityBuilder {

    @Override
    public HttpEntity build() throws IOException {
        Object content = email;
        if (content == null) {
            content = model;
        }
        if (content == null) {
            content = map;
        }
        if (content == null) {
            content = list;
        }
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
