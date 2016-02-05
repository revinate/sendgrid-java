package com.revinate.sendgrid.net;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpResponseReader {

    public String readContent(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity);
    }
}
