package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Whitelabel;
import com.revinate.sendgrid.model.WhitelabelValidation;
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

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinkWhitelabelResourceTest extends BaseSendGridTest {

    private static final String ID = "1";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    LinkWhitelabelResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new LinkWhitelabelResource("https://api.sendgrid.com/v3/whitelabel/links", client, credential, ID);
    }

    @Test
    public void retrieve_shouldReturnWhitelabel() throws Exception {
        Whitelabel response = JsonUtils.fromJson(readFile("/responses/link-whitelabel.json"), Whitelabel.class);

        when(client.get("https://api.sendgrid.com/v3/whitelabel/links/" + ID,
                Whitelabel.class, credential)).thenReturn(response);

        Whitelabel whitelabel = resource.retrieve();

        assertThat(whitelabel, sameInstance(response));
    }

    @Test
    public void update_shouldThrowUnsupported() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.update(new Whitelabel());
    }

    @Test
    public void delete_shouldDeleteWhitelabel() throws Exception {
        resource.delete();

        verify(client).delete("https://api.sendgrid.com/v3/whitelabel/links/" + ID, credential);
    }

    @Test
    public void validate_shouldReturnValidation() throws Exception {
        WhitelabelValidation response = JsonUtils.fromJson(readFile("/responses/link-whitelabel-validation.json"), WhitelabelValidation.class);

        when(client.post("https://api.sendgrid.com/v3/whitelabel/links/" + ID + "/validate",
                WhitelabelValidation.class, credential)).thenReturn(response);

        WhitelabelValidation validate = resource.validate();

        assertThat(validate, sameInstance(response));
    }
}
