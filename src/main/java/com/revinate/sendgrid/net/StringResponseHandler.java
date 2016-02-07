package com.revinate.sendgrid.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class StringResponseHandler implements ResponseHandler<String> {

    private HttpEntityReader reader;

    public StringResponseHandler() {
        this(new HttpEntityReader());
    }

    public StringResponseHandler(HttpEntityReader reader) {
        this.reader = reader;
    }

    @Override
    public String handleResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String responseBody = null;
        if (entity != null) {
            responseBody = reader.readContent(entity);
        }

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new HttpResponseException(statusCode, responseBody);
        } else {
            return responseBody;
        }
    }

    public static class HttpEntityReader {
        public String readContent(HttpEntity entity) throws IOException {
            return EntityUtils.toString(entity);
        }
    }
}
