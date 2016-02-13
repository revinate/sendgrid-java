package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.EventWebhookSettings;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventWebhookSettingsResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    EventWebhookSettingsResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new EventWebhookSettingsResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void retrieve_shouldReturnSettings() throws Exception {
        EventWebhookSettings response = JsonUtils.fromJson(readFile("/responses/event-webhook-settings.json"),
                EventWebhookSettings.class);

        when(client.get("https://api.sendgrid.com/v3/user/webhooks/event/settings",
                EventWebhookSettings.class, credential)).thenReturn(response);

        EventWebhookSettings settings = resource.retrieve();

        assertThat(settings, sameInstance(response));
    }

    @Test
    public void create_shouldThrowUnsupported() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.create(new EventWebhookSettings());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPatchAndReturnSettings() throws Exception {
        EventWebhookSettings response = JsonUtils.fromJson(readFile("/responses/event-webhook-settings.json"),
                EventWebhookSettings.class);
        EventWebhookSettings settings = new EventWebhookSettings("http://sendgrid", true);

        when(client.patch(any(String.class), any(Class.class),
                any(Credential.class), any(ApiKey.class), any(RequestType.class))).thenReturn(response);

        EventWebhookSettings settings1 = resource.update(settings);

        assertThat(settings1, sameInstance(response));

        ArgumentCaptor<EventWebhookSettings> captor = ArgumentCaptor.forClass(EventWebhookSettings.class);
        verify(client).patch(eq("https://api.sendgrid.com/v3/user/webhooks/event/settings"),
                eq(EventWebhookSettings.class), eq(credential), captor.capture(), eq(RequestType.JSON));

        EventWebhookSettings settings2 = captor.getValue();

        assertThat(settings2, notNullValue());
        assertThat(settings2.getUrl(), equalTo(settings.getUrl()));
        assertThat(settings2.getEnabled(), equalTo(settings.getEnabled()));
    }

    @Test
    public void delete_shouldThrowUnsupported() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.delete();
    }
}
