package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.model.Response;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MailResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    MailResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new MailResource("https://api.sendgrid.com/api", client, credential);
    }

    @Test
    public void send_shouldPostEmailAndReturnResponse() throws Exception {
        Response response = JsonUtils.fromJson(readFile("/responses/response.json"), Response.class);
        Email email = new Email();
        email.setFrom("test1@email.com");
        email.addTo("test1@email.com");

        when(client.post("https://api.sendgrid.com/api/mail.send.json", email,
                Response.class, credential)).thenReturn(response);

        Response response1 = resource.send(email);

        Assert.assertThat(response1, sameInstance(response));
    }
}
