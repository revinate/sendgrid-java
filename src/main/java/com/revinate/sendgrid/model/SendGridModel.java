package com.revinate.sendgrid.model;

import com.revinate.sendgrid.net.AcceptsHttpEntityBuilder;
import com.revinate.sendgrid.net.HttpEntityBuilder;
import com.revinate.sendgrid.util.JsonUtils;

import java.io.IOException;

public abstract class SendGridModel implements AcceptsHttpEntityBuilder {

    @Override
    public void accept(HttpEntityBuilder builder) {
        builder.setContent(this);
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
