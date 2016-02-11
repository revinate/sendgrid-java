package com.revinate.sendgrid.net;

import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.model.SendGridModel;
import com.revinate.sendgrid.model.SendGridModelVisitor;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class HttpEntityBuilder implements SendGridModelVisitor {

    protected Credential credential;
    protected Email email;
    protected SendGridModel model;
    protected Map<String, Object> map;
    protected List<String> list;

    public static HttpEntityBuilder create(RequestType requestType) {
        switch (requestType) {
            case MULTIPART:
                return new MultipartHttpEntityBuilder();
            default:
                return new JsonHttpEntityBuilder();
        }
    }

    @Override
    public void visit(Email email) {
        setEmail(email);
    }

    @Override
    public void visit(SendGridModel model) {
        setModel(model);
    }

    public HttpEntityBuilder setCredential(Credential credential) {
        this.credential = credential;
        return this;
    }

    public HttpEntityBuilder setEmail(Email email) {
        clearContent();
        this.email = email;
        return this;
    }

    public HttpEntityBuilder setModel(SendGridModel model) {
        clearContent();
        this.model = model;
        return this;
    }

    public HttpEntityBuilder setMap(Map<String, Object> map) {
        clearContent();
        this.map = map;
        return this;
    }

    public HttpEntityBuilder setList(List<String> list) {
        clearContent();
        this.list = list;
        return this;
    }

    public abstract HttpEntity build() throws IOException;

    private void clearContent() {
        this.email = null;
        this.model = null;
        this.map = null;
        this.list = null;
    }
}
