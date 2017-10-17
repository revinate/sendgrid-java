package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.IpWhitelabel;
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
public class IpWhitelabelResourceTest extends BaseSendGridTest {

    private static final String ID = "1";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    IpWhitelabelResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IpWhitelabelResource("https://api.sendgrid.com/v3/whitelabel/ips", client, credential, ID);
    }

    @Test
    public void retrieve_shouldReturnWhitelabel() throws Exception {
        IpWhitelabel response = JsonUtils.fromJson(readFile("/responses/ip-whitelabel.json"), IpWhitelabel.class);

        when(client.get("https://api.sendgrid.com/v3/whitelabel/ips/" + ID,
                IpWhitelabel.class, credential)).thenReturn(response);

        IpWhitelabel ipWhitelabel = resource.retrieve();

        assertThat(ipWhitelabel, sameInstance(response));
    }

    @Test
    public void update_shouldThrowUnsupported() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.update(new IpWhitelabel());
    }

    @Test
    public void delete_shouldDeleteWhitelabel() throws Exception {
        resource.delete();

        verify(client).delete("https://api.sendgrid.com/v3/whitelabel/ips/" + ID, credential);
    }

    @Test
    public void validate_shouldReturnValidation() throws Exception {
        WhitelabelValidation response = JsonUtils.fromJson(readFile("/responses/ip-whitelabel-validation.json"), WhitelabelValidation.class);

        when(client.post("https://api.sendgrid.com/v3/whitelabel/ips/" + ID + "/validate",
                WhitelabelValidation.class, credential)).thenReturn(response);

        WhitelabelValidation validate = resource.validate();

        assertThat(validate, sameInstance(response));
    }
}
