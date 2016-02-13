package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.MailSetting;
import com.revinate.sendgrid.model.MailSettingsResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MailSettingsResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    MailSettingsResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new MailSettingsResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void entity_shouldReturnResource() throws Exception {
        MailSetting mailSetting = new MailSetting();
        mailSetting.setName("forward_spam");

        MailSettingResource subresource = resource.entity(mailSetting);

        assertThat(subresource, notNullValue());
        assertThat(subresource.getId(), equalTo("forward_spam"));
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/mail_settings"));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void list_shouldReturnMailSettings() throws Exception {
        MailSettingsResponse response = JsonUtils.fromJson(readFile("/responses/mail-settings.json"),
                MailSettingsResponse.class);

        when(client.get("https://api.sendgrid.com/v3/mail_settings", MailSettingsResponse.class, credential))
                .thenReturn(response);

        List<MailSetting> settings = resource.list();

        assertThat(settings, sameInstance(response.getData()));
    }

    @Test
    public void create_shouldThrowUnsupported() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.create(new MailSetting());
    }
}
