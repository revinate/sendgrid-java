package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.MailSetting;
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
public class MailSettingResourceTest extends BaseSendGridTest {

    private static final String NAME = "forward_spam";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    MailSettingResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new MailSettingResource("https://api.sendgrid.com/v3/mail_settings", client, credential, NAME);
    }

    @Test
    public void retrieve_shouldReturnSetting() throws Exception {
        MailSetting response = JsonUtils.fromJson(readFile("/responses/mail-setting.json"), MailSetting.class);

        when(client.get("https://api.sendgrid.com/v3/mail_settings/" + NAME,
                MailSetting.class, credential)).thenReturn(response);

        MailSetting setting = resource.retrieve();

        assertThat(setting, sameInstance(response));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPatchAndReturnSetting() throws Exception {
        MailSetting response = JsonUtils.fromJson(readFile("/responses/mail-setting.json"), MailSetting.class);
        MailSetting setting = new MailSetting(NAME, true, "test1@email.com");

        when(client.patch(any(String.class), any(Class.class),
                any(Credential.class), any(MailSetting.class), any(RequestType.class))).thenReturn(response);

        MailSetting setting1 = resource.update(setting);

        assertThat(setting1, sameInstance(response));

        ArgumentCaptor<MailSetting> captor = ArgumentCaptor.forClass(MailSetting.class);
        verify(client).patch(eq("https://api.sendgrid.com/v3/mail_settings/" + NAME),
                eq(MailSetting.class), eq(credential), captor.capture(), eq(RequestType.JSON));

        MailSetting setting2 = captor.getValue();

        assertThat(setting2, notNullValue());
        assertThat(setting2.getEmail(), equalTo(setting.getEmail()));
        assertThat(setting2.getEnabled(), equalTo(setting.getEnabled()));
    }

    @Test
    public void delete_shouldThrowUnsupported() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.delete();
    }
}
