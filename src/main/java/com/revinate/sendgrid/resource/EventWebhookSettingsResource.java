package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.EventWebhookSettings;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;

public class EventWebhookSettingsResource extends SingularEntityResource<EventWebhookSettings> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "user/webhooks/event/settings";

    public EventWebhookSettingsResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, EventWebhookSettings.class);
    }

    @Override
    public EventWebhookSettings create(EventWebhookSettings settings) throws SendGridException {
        throw unsupported();
    }

    @Override
    public EventWebhookSettings update(EventWebhookSettings settings) throws SendGridException {
        return client.patch(getUrl(), EventWebhookSettings.class, credential, settings, RequestType.JSON);
    }

    @Override
    public void delete() throws SendGridException {
        throw unsupported();
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
