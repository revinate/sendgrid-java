package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.model.IpPool;
import com.revinate.sendgrid.model.IpPoolCollection;
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
public class IpPoolsResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    IpPoolsResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IpPoolsResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void entity_shouldReturnResource() throws Exception {
        IpPool ipPool = new IpPool();
        ipPool.setName("test");

        IpPoolResource subresource = resource.entity(ipPool);

        assertThat(subresource, notNullValue());
        assertThat(subresource.getId(), equalTo("test"));
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/ips/pools"));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void list_shouldReturnIpPools() throws Exception {
        IpPoolCollection response = JsonUtils.fromJson(readFile("/responses/ip-pools.json"),
                IpPoolCollection.class);

        when(client.get("https://api.sendgrid.com/v3/ips/pools", IpPoolCollection.class, credential))
                .thenReturn(response);

        List<IpPool> ipPools = resource.list();

        assertThat(ipPools, sameInstance(response.getData()));
    }

    @Test
    public void create_shouldPostAndReturnIpPool() throws Exception {
        IpPool response = JsonUtils.fromJson(readFile("/responses/ip-pool.json"), IpPool.class);
        IpPool ipPool = new IpPool();
        ipPool.setName("transactional");

        when(client.post("https://api.sendgrid.com/v3/ips/pools", ipPool,
                IpPool.class, credential)).thenReturn(response);

        IpPool ipPool1 = resource.create(ipPool);

        assertThat(ipPool1, sameInstance(response));
    }
}
