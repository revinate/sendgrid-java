package com.revinate.sendgrid.net;

import com.revinate.sendgrid.model.Email;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.util.List;

public interface HttpEntityBuilder {

    HttpEntityBuilder setContent(Email email);
    HttpEntityBuilder setContent(Object object);
    HttpEntity build() throws IOException;
    List<Header> getHeaders();
}
