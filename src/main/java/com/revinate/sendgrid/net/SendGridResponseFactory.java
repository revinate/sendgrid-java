package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SendGridResponseFactory implements ResponseHandler<SendGridResponse> {

    private HttpEntityReader reader;

    public SendGridResponseFactory() {
        this(new HttpEntityReader());
    }

    public SendGridResponseFactory(HttpEntityReader reader) {
        this.reader = reader;
    }

    @Override
    public SendGridResponse handleResponse(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return new SendGridResponse(new ApiException("no response body"));
        }
        String responseBody = reader.readContent(entity);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            return new SendGridResponse(createException(statusCode, responseBody));
        } else {
            return new SendGridResponse(responseBody);
        }
    }

    private SendGridException createException(int statusCode, String responseBody) {
        switch (statusCode) {
            case 400:
                return new InvalidRequestException(responseBody);
            case 401:
                return new AuthenticationException(responseBody);
            case 404:
                return new NotFoundException(responseBody);
            default:
                return new ApiException(responseBody);
        }
    }

    public static class HttpEntityReader {
        public String readContent(HttpEntity entity) throws IOException {
            return EntityUtils.toString(entity);
        }
    }
}
