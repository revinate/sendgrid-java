package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.IpCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
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
public class IpsResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    IpsResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IpsResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void entity_shouldReturnResource() throws Exception {
        Ip ip = new Ip();
        ip.setIp("127.0.0.1");

        IpResource subresource = resource.entity(ip);

        assertThat(subresource, notNullValue());
        assertThat(subresource.getId(), equalTo("127.0.0.1"));
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/ips"));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void list_shouldReturnIps() throws Exception {
        IpCollection response = JsonUtils.fromJson(readFile("/responses/ips.json"),
                IpCollection.class);

        when(client.get("https://api.sendgrid.com/v3/ips", IpCollection.class, credential))
                .thenReturn(response);

        List<Ip> ips = resource.list();

        assertThat(ips, sameInstance(response.getData()));
    }

    @Test
    public void create_shouldPostAndReturnIp() throws Exception {
        Ip response = JsonUtils.fromJson(readFile("/responses/ip.json"), Ip.class);
        Ip ip = new Ip();
        ip.setIp("127.0.0.1");
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools",
                client, credential, "transactional").ips();

        when(client.post("https://api.sendgrid.com/v3/ips/pools/transactional/ips",
                Ip.class, credential, ip, RequestType.JSON)).thenReturn(response);

        Ip ip1 = resource.create(ip);

        assertThat(ip1, sameInstance(response));
    }
}
