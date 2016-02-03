package com.revinate.sendgrid.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revinate.sendgrid.SendGrid;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;

public class SendGridResource {

    public String toJson() {
        try {
            return SendGrid.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public HttpEntity toHttpEntity() {
        return EntityBuilder.create().setText(toJson()).build();
    }

    @Override
    public String toString() {
        try {
            return SendGrid.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
