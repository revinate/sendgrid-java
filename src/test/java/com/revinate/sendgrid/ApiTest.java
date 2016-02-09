package com.revinate.sendgrid;

import com.revinate.sendgrid.exception.ResourceNotFoundException;
import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.Ip;
import com.revinate.sendgrid.model.IpPool;
import com.revinate.sendgrid.model.Subuser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class ApiTest {

    private static final String API_KEY = System.getenv("SENDGRID_API_KEY");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    SendGrid sendGrid;

    @Before
    public void setUp() throws Exception {
        assumeThat(API_KEY, notNullValue());
        sendGrid = new SendGrid(API_KEY);
    }

    @Test
    public void getResources_shouldReturnResources() throws Exception {
        List<Subuser> subusers = sendGrid.subusers().list();
        assertThat(subusers, notNullValue());

        Subuser subuser = sendGrid.subusers().retrieve("testsubuser123");
        assertThat(subuser, notNullValue());

        List<Ip> ips = sendGrid.ips().list();
        assertThat(ips, notNullValue());

        List<IpPool> ipPools = sendGrid.ipPools().list();
        assertThat(ipPools, notNullValue());
    }

    @Test
    public void getResourcesOnBehalfOf_shouldReturnResources() throws Exception {
        List<IpPool> ipPools = sendGrid.onBehalfOf("testsubuser123").ipPools().list();
        assertThat(ipPools, notNullValue());

        IpPool ipPool = sendGrid.onBehalfOf("testsubuser123").ipPools().retrieve("transactional");
        assertThat(ipPool, notNullValue());

        List<ApiKey> apiKeys = sendGrid.onBehalfOf("testsubuser123").apiKeys().list();
        assertThat(apiKeys, notNullValue());
    }

    @Test
    public void createSubuser_shouldReturnSubuser() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setUsername("testsubuser124");
        subuser.setPassword("secretpasswordfortesting124");
        subuser.setEmail("no-reply@email.com");

        List<Ip> ips = sendGrid.ips().list();
        for (Ip ip : ips) {
            if (ip.getSubusers() == null || ip.getSubusers().isEmpty()) {
                subuser.addIp(ip.getIp());
                break;
            }
        }

        Subuser subuser1 = sendGrid.subusers().create(subuser);

        assertThat(subuser1.getUsername(), equalTo("testsubuser124"));

        sendGrid.subusers().delete(subuser1);

        thrown.expect(ResourceNotFoundException.class);
        thrown.expectMessage("Subuser not found");

        sendGrid.subusers().retrieve(subuser1.getUsername());
    }

    @Test
    public void createApiKey_shouldReturnApiKey() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("testapikey");
        apiKey.addScope("mail.send");

        ApiKey apiKey1 = sendGrid.apiKeys().create(apiKey);

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("testapikey"));
        assertThat(apiKey1.getApiKeyId(), notNullValue());
        assertThat(apiKey1.getApiKey(), notNullValue());

        ApiKey apiKey2 = sendGrid.apiKey(apiKey1.getApiKeyId()).retrieve();

        assertThat(apiKey2, notNullValue());
        assertThat(apiKey2.getName(), equalTo("testapikey"));
        assertThat(apiKey2.getApiKeyId(), equalTo(apiKey1.getApiKeyId()));

        sendGrid.apiKey(apiKey1).delete();

        ApiKey apiKey3 = sendGrid.apiKey(apiKey1.getApiKeyId()).retrieve();

        // SendGrid does not throw a 404 here, so we get an API key with unset fields
        assertThat(apiKey3.getApiKeyId(), nullValue());
    }

    @Test
    public void createApiKeyOnBehalfOf_shouldReturnApiKey() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("testapikey");
        apiKey.addScope("mail.send");

        ApiKey apiKey1 = sendGrid.onBehalfOf("testsubuser123").apiKeys().create(apiKey);

        assertThat(apiKey1, notNullValue());
        assertThat(apiKey1.getName(), equalTo("testapikey"));
        assertThat(apiKey1.getApiKeyId(), notNullValue());
        assertThat(apiKey1.getApiKey(), notNullValue());

        ApiKey apiKey2 = sendGrid.onBehalfOf("testsubuser123").apiKey(apiKey1.getApiKeyId()).retrieve();

        assertThat(apiKey2, notNullValue());
        assertThat(apiKey2.getName(), equalTo("testapikey"));
        assertThat(apiKey2.getApiKeyId(), equalTo(apiKey1.getApiKeyId()));

        sendGrid.onBehalfOf("testsubuser123").apiKey(apiKey1).delete();

        ApiKey apiKey3 = sendGrid.onBehalfOf("testsubuser123").apiKey(apiKey1.getApiKeyId()).retrieve();

        // SendGrid does not throw a 404 here, so we get an API key with unset fields
        assertThat(apiKey3.getApiKeyId(), nullValue());
    }
}
