package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IpPoolResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    IpPoolResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools", client, credential);
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
    public void retrieve_shouldReturnIpPool() throws Exception {
        IpPool response = JsonUtils.fromJson(readFile("/responses/ip-pool.json"), IpPool.class);

        when(client.get("https://api.sendgrid.com/v3/ips/pools/" + response.getName(),
                IpPool.class, credential)).thenReturn(response);

        IpPool ipPool = resource.retrieve(response.getName());

        assertThat(ipPool, sameInstance(response));
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

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPutAndReturnIpPool() throws Exception {
        IpPool response = JsonUtils.fromJson(readFile("/responses/ip-pool.json"), IpPool.class);
        IpPool ipPool = new IpPool();
        ipPool.setName(response.getName());

        when(client.put(any(String.class), any(IpPool.class), any(Class.class),
                any(Credential.class))).thenReturn(response);

        IpPool ipPool1 = resource.update(ipPool);

        assertThat(ipPool1, sameInstance(response));

        ArgumentCaptor<IpPool> captor = ArgumentCaptor.forClass(IpPool.class);
        verify(client).put(eq("https://api.sendgrid.com/v3/ips/pools/" + response.getName()),
                captor.capture(), eq(IpPool.class), eq(credential));

        IpPool ipPool2 = captor.getValue();

        assertThat(ipPool2, notNullValue());
        assertThat(ipPool2.getName(), equalTo(ipPool.getName()));
    }

    @Test
    public void update_shouldHandleMissingId() throws Exception {
        IpPool ipPool = new IpPool();

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.update(ipPool);
    }

    @Test
    public void delete_shouldDeleteIpPool() throws Exception {
        IpPool ipPool = new IpPool();
        ipPool.setName("transactional");

        resource.delete(ipPool);

        verify(client).delete("https://api.sendgrid.com/v3/ips/pools/" + ipPool.getName(), credential);
    }

    @Test
    public void delete_shouldHandleMissingId() throws Exception {
        IpPool ipPool = new IpPool();

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.delete(ipPool);
    }
}
