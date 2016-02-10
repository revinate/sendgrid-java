package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.IpPool;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IpPoolResourceTest extends BaseSendGridTest {

    private static final String POOL_NAME = "transactional";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    IpPoolResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools", client, credential, POOL_NAME);
    }

    @Test
    public void ips_shouldReturnResource() throws Exception {
        IpsResource subresource = resource.ips();

        assertThat(subresource, notNullValue());
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/" + POOL_NAME));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void ips_shouldHandleMissingId() throws Exception {
        IpPool ipPool = new IpPool();
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools", client, credential, ipPool);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.ips();
    }

    @Test
    public void retrieve_shouldReturnIpPool() throws Exception {
        IpPool response = JsonUtils.fromJson(readFile("/responses/ip-pool.json"), IpPool.class);

        when(client.get("https://api.sendgrid.com/v3/ips/pools/" + POOL_NAME,
                IpPool.class, credential)).thenReturn(response);

        IpPool ipPool = resource.retrieve();

        assertThat(ipPool, sameInstance(response));
    }

    @Test
    public void retrieve_shouldHandleMissingId() throws Exception {
        IpPool ipPool = new IpPool();
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools", client, credential, ipPool);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.retrieve();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPutAndReturnIpPool() throws Exception {
        IpPool response = JsonUtils.fromJson(readFile("/responses/ip-pool.json"), IpPool.class);
        IpPool ipPool = new IpPool();
        ipPool.setName(POOL_NAME);

        when(client.put(any(String.class), any(IpPool.class), any(Class.class),
                any(Credential.class))).thenReturn(response);

        IpPool ipPool1 = resource.update(ipPool);

        assertThat(ipPool1, sameInstance(response));

        ArgumentCaptor<IpPool> captor = ArgumentCaptor.forClass(IpPool.class);
        verify(client).put(eq("https://api.sendgrid.com/v3/ips/pools/" + POOL_NAME),
                captor.capture(), eq(IpPool.class), eq(credential));

        IpPool ipPool2 = captor.getValue();

        assertThat(ipPool2, notNullValue());
        assertThat(ipPool2.getName(), equalTo(POOL_NAME));
    }

    @Test
    public void update_shouldHandleMissingId() throws Exception {
        IpPool ipPool = new IpPool();
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools", client, credential, ipPool);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.update(ipPool);
    }

    @Test
    public void partialUpdate_shouldThrowUnsupported() throws Exception {
        Map<String, Object> requestObject = new HashMap<String, Object>();

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.partialUpdate(requestObject);
    }

    @Test
    public void delete_shouldDeleteIpPool() throws Exception {
        resource.delete();

        verify(client).delete("https://api.sendgrid.com/v3/ips/pools/" + POOL_NAME, credential);
    }

    @Test
    public void delete_shouldHandleMissingId() throws Exception {
        IpPool ipPool = new IpPool();
        resource = new IpPoolResource("https://api.sendgrid.com/v3/ips/pools", client, credential, ipPool);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.delete();
    }
}
