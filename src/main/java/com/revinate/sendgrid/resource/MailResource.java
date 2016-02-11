package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.model.Response;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;

public class MailResource extends SendGridResource {

    public static final ApiVersion API_VERSION = ApiVersion.V2;
    public static final String ENDPOINT = "mail";

    public MailResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public Response send(Email email) throws SendGridException {
        return client.post(getUrl("send", "json"), Response.class, credential, email, RequestType.MULTIPART);
    }

    protected String getUrl(String action, String format) {
        return String.format("%s/%s.%s.%s", baseUrl, getEndpoint(), action, format);
    }

    protected String getEndpoint() {
        return ENDPOINT;
    }
}
