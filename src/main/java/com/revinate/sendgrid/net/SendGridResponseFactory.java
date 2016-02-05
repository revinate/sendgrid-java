package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SendGridResponseFactory {

    private HttpEntityReader reader;

    public SendGridResponseFactory() {
        this(new HttpEntityReader());
    }

    public SendGridResponseFactory(HttpEntityReader reader) {
        this.reader = reader;
    }

    public SendGridResponse create(HttpResponse response) throws IOException {
        String responseBody = reader.readContent(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            return new SendGridResponse(responseBody);
        } else {
            return new SendGridResponse(createException(statusCode, responseBody));
        }
    }

    private SendGridException createException(int statusCode, String responseBody) {
        switch (statusCode) {
            case 400:
                return new InvalidRequestException(responseBody);
            case 401:
                return new NotAuthorizedException(responseBody);
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
