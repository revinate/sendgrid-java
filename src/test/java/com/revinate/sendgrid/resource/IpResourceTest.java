package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Ip;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IpResourceTest extends BaseSendGridTest {

    private static final String IP = "127.0.0.1";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    IpResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IpResource("https://api.sendgrid.com/v3/ips", client, credential, IP);
    }

    @Test
    public void retrieve_shouldReturnIp() throws Exception {
        Ip response = JsonUtils.fromJson(readFile("/responses/ip.json"), Ip.class);

        when(client.get("https://api.sendgrid.com/v3/ips/" + IP,
                Ip.class, credential)).thenReturn(response);

        Ip ip = resource.retrieve();

        assertThat(ip, sameInstance(response));
    }
    @Test
    public void retrieve_shouldHandleMissingId() throws Exception {
        Ip ip = new Ip();
        resource = new IpResource("https://api.sendgrid.com/v3/ips", client, credential, ip);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.retrieve();
    }

    @Test
    public void update_shouldThrowUnsupported() throws Exception {
        Ip ip = new Ip();
        ip.setIp("127.0.0.1");

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.update(ip);
    }

    @Test
    public void partialUpdate_shouldThrowUnsupported() throws Exception {
        Map<String, Object> requestObject = new HashMap<String, Object>();

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.partialUpdate(requestObject);
    }

    @Test
    public void delete_shouldDeleteIp() throws Exception {
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools",
                client, credential, "transactional").ip("127.0.0.1");

        resource.delete();

        verify(client).delete("https://api.sendgrid.com/v3/ips/pools/transactional/ips/127.0.0.1",
                credential);
    }
}
