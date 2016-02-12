package com.revinate.sendgrid;

import com.revinate.sendgrid.exception.ResourceNotFoundException;
import com.revinate.sendgrid.model.*;
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
        sendGrid = SendGrid.create(API_KEY).build();
    }

    @Test
    public void getResources_shouldReturnResources() throws Exception {
        List<Subuser> subusers = sendGrid.subusers().list();
        assertThat(subusers, notNullValue());

        Subuser subuser = sendGrid.subuser("testsubuser123").retrieve();
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

        IpPool ipPool = sendGrid.onBehalfOf("testsubuser123").ipPool("transactional").retrieve();
        assertThat(ipPool, notNullValue());

        List<ApiKey> apiKeys = sendGrid.onBehalfOf("testsubuser123").apiKeys().list();
        assertThat(apiKeys, notNullValue());
    }

    @Test
    public void createSubuser_shouldReturnSubuser() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setUsername("testsubuser124");
        subuser.setPassword("secretpasswordfortesting124");
        subuser.setEmail("sendgridjava@mailinator.com");

        List<Ip> ips = sendGrid.ips().list();
        for (Ip ip : ips) {
            if (ip.getSubusers() == null || ip.getSubusers().isEmpty()) {
                subuser.addIp(ip.getIp());
                break;
            }
        }

        Subuser subuser1 = sendGrid.subusers().create(subuser);

        assertThat(subuser1.getUsername(), equalTo("testsubuser124"));

        sendGrid.subuser(subuser1).delete();

        thrown.expect(ResourceNotFoundException.class);
        thrown.expectMessage("Subuser not found");

        sendGrid.subuser(subuser1.getUsername()).retrieve();
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

    @Test
    public void createIpPool_shouldReturnIpPool() throws Exception {
        IpPool ipPool = new IpPool();
        ipPool.setName("testpool");

        IpPool ipPool1 = sendGrid.ipPools().create(ipPool);

        assertThat(ipPool1, notNullValue());
        assertThat(ipPool1.getName(), equalTo(ipPool.getName()));

        IpPool ipPool2 = sendGrid.ipPool(ipPool1.getName()).retrieve();

        assertThat(ipPool2, notNullValue());
        assertThat(ipPool2.getName(), equalTo(ipPool1.getName()));
        assertThat(ipPool2.getIps(), emptyCollectionOf(Ip.class));
        sendGrid.ipPool(ipPool1).delete();

        thrown.expect(ResourceNotFoundException.class);

        sendGrid.ipPool(ipPool1.getName()).retrieve();
    }

    @Test
    public void createMonitor_shouldReturnMonitor() throws Exception {
        Monitor monitor = new Monitor();
        monitor.setEmail("sendgridjava@mailinator.com");
        monitor.setFrequency(5000);

        Monitor monitor1 = sendGrid.subuser("testsubuser123").monitor().create(monitor);

        assertThat(monitor1, notNullValue());
        assertThat(monitor1.getEmail(), equalTo(monitor.getEmail()));
        assertThat(monitor1.getFrequency(), equalTo(monitor.getFrequency()));

        Monitor monitor2 = sendGrid.subuser("testsubuser123").monitor().retrieve();

        assertThat(monitor2, notNullValue());
        assertThat(monitor2.getEmail(), equalTo(monitor.getEmail()));
        assertThat(monitor2.getFrequency(), equalTo(monitor.getFrequency()));

        sendGrid.subuser("testsubuser123").monitor().delete();

        thrown.expect(ResourceNotFoundException.class);

        sendGrid.subuser("testsubuser123").monitor().retrieve();
    }

    @Test
    public void sendEmail_shouldSendEmail() throws Exception {
        Email email = new Email();
        email.setFrom("sendgridjava@mailinator.com");
        email.addTo("sendgridjava@mailinator.com");
        email.setSubject("Test");
        email.setText("This is a test.");
        email.setAttachment("test.txt", getClass().getResourceAsStream("/test.txt"));
        email.setAttachment("image.png", getClass().getResourceAsStream("/image.png"));

        Response response = sendGrid.mail().send(email);

        assertThat(response, notNullValue());
        assertThat(response.getMessage(), equalTo("success"));
    }

    @Test
    public void sendEmail_shouldSendEmailUsingSmtpApiTo() throws Exception {
        Email email = new Email();
        email.setFrom("sendgridjava@mailinator.com");
        email.addSmtpApiTo("sendgridjava@mailinator.com", "SendGrid Java");
        email.setSubject("Test");
        email.setText("This is a test.");

        Response response = sendGrid.mail().send(email);

        assertThat(response, notNullValue());
        assertThat(response.getMessage(), equalTo("success"));
    }
}
