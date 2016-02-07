package com.revinate.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;

import java.io.IOException;

public abstract class SendGridResource {

    @JsonIgnore
    public abstract String getPathId();

    public String toJson() {
        try {
            return JsonUtils.toJson(this);
        } catch (IOException e) {
            return null;
        }
    }

    public HttpEntity toHttpEntity() {
        return EntityBuilder.create().setText(toJson()).build();
    }

    @Override
    public String toString() {
        try {
            return String.format("<%s> JSON: %s", super.toString(),
                    JsonUtils.toPrettyPrintJson(this));
        } catch (IOException e) {
            return super.toString();
        }
    }
}
