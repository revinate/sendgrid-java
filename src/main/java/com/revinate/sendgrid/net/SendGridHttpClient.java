package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.HttpException;
import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class SendGridHttpClient {

    private final HttpClient client;
    private HttpResponseReader responseReader;

    public SendGridHttpClient(HttpClient client) {
        this.client = client;
    }

    public String get(String url, Credential credential) throws SendGridException {
        HttpGet request = new HttpGet(url);
        request.setHeaders(credential.toHttpHeaders());

        try {
            HttpResponse response = client.execute(request);
            return responseReader.readContent(response.getEntity());
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    void setResponseReader(HttpResponseReader responseReader) {
        this.responseReader = responseReader;
    }
}
