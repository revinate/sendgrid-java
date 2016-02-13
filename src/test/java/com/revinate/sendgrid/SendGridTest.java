package com.revinate.sendgrid;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.net.auth.OnBehalfOfCredential;
import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import com.revinate.sendgrid.resource.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SendGridTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String API_KEY = "token";
    private static final String BASE_URL = "http://sendgrid";
    private static final int MAX_CONNECTIONS = 50;

    @Mock
    SendGridHttpClient client;

    SendGrid sendGrid;

    @Before
    public void setUp() throws Exception {
        sendGrid = new SendGrid(BASE_URL, client, new ApiKeyCredential(API_KEY));
    }

    @Test
    public void version_shouldMatchGradleVersion() throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./build.gradle"));
            String line = br.readLine();
            String regex = "version\\s*=\\s*'" + SendGrid.VERSION + "'";

            while (line != null) {
                if (line.matches(regex)) {
                    br.close();
                    return;
                }
                line = br.readLine();
            }
            br.close();
            fail("build.gradle version does not match");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void onBehalfOf_shouldOverlayCredential() throws Exception {
        ApiKeysResource resource = sendGrid.onBehalfOf("username2").apiKeys();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/v3"));
        assertThat(resource.getClient(), sameInstance(client));

        Credential credential = resource.getCredential();
        assertThat(credential, notNullValue());
        assertThat(credential, instanceOf(OnBehalfOfCredential.class));

        OnBehalfOfCredential onBehalfOfCredential = (OnBehalfOfCredential) credential;
        assertThat(onBehalfOfCredential.getCredential(), sameInstance(sendGrid.getCredential()));
        assertThat(onBehalfOfCredential.getUsername(), equalTo("username2"));
    }

    @Test
    public void onBehalfOf_shouldOverrideCredential() throws Exception {
        Credential credential = mock(Credential.class);
        ApiKeysResource resource = sendGrid.onBehalfOf(credential).apiKeys();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/v3"));
        assertThat(resource.getClient(), sameInstance(client));
        assertThat(resource.getCredential(), sameInstance(credential));
    }

    @Test
    public void account_shouldReturnResource() throws Exception {
        AccountResource resource = sendGrid.account();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/v3"));
        assertThat(resource.getClient(), sameInstance(client));
        assertThat(resource.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void apiKeys_shouldReturnResource() throws Exception {
        ApiKeysResource resource = sendGrid.apiKeys();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/v3"));
        assertThat(resource.getClient(), sameInstance(client));
        assertThat(resource.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void subusers_shouldReturnResource() throws Exception {
        SubusersResource resource = sendGrid.subusers();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/v3"));
        assertThat(resource.getClient(), sameInstance(client));
        assertThat(resource.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void ips_shouldReturnResource() throws Exception {
        IpsResource resource = sendGrid.ips();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/v3"));
        assertThat(resource.getClient(), sameInstance(client));
        assertThat(resource.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void ipPools_shouldReturnResource() throws Exception {
        IpPoolsResource resource = sendGrid.ipPools();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/v3"));
        assertThat(resource.getClient(), sameInstance(client));
        assertThat(resource.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void mail_shouldReturnResource() throws Exception {
        MailResource resource = sendGrid.mail();

        assertThat(resource, notNullValue());
        assertThat(resource.getBaseUrl(), equalTo(BASE_URL + "/api"));
        assertThat(resource.getClient(), sameInstance(client));
        assertThat(resource.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void close_shouldCloseUnderlyingClient() throws Exception {
        sendGrid.close();

        verify(client).close();
    }

    @Test
    public void builder_shouldAcceptApiKey() throws Exception {
        sendGrid = SendGrid.create(API_KEY).build();

        assertThat(sendGrid, notNullValue());
        assertThat(sendGrid.getCredential(), equalTo(
                (Credential) new ApiKeyCredential(API_KEY)));
    }

    @Test
    public void builder_shouldAcceptUsernamePassword() throws Exception {
        sendGrid = SendGrid.create(USERNAME, PASSWORD).build();

        assertThat(sendGrid, notNullValue());
        assertThat(sendGrid.getCredential(), equalTo(
                (Credential) new UsernamePasswordCredential(USERNAME, PASSWORD)));
    }

    @Test
    public void builder_shouldAcceptCustomCredential() throws Exception {
        Credential credential = mock(Credential.class);
        sendGrid = SendGrid.create(credential).build();

        assertThat(sendGrid, notNullValue());
        assertThat(sendGrid.getCredential(), sameInstance(credential));
    }

    @Test
    public void builder_shouldAcceptMaxConnections() throws Exception {
        sendGrid = SendGrid.create(API_KEY).setMaxConnections(MAX_CONNECTIONS).build();

        assertThat(sendGrid, notNullValue());
        assertThat(sendGrid.getClient(), notNullValue());
    }

    @Test
    public void builder_shouldAcceptCustomHttpClient() throws Exception {
        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        sendGrid = SendGrid.create(API_KEY).setHttpClient(httpClient).build();

        assertThat(sendGrid, notNullValue());
        assertThat(sendGrid.getClient().getClient(), sameInstance(httpClient));
    }

    @Test
    public void builder_shouldAcceptCustomClient() throws Exception {
        sendGrid = SendGrid.create(API_KEY).setClient(client).build();

        assertThat(sendGrid, notNullValue());
        assertThat(sendGrid.getClient(), sameInstance(client));
    }

    @Test
    public void builder_shouldAcceptCustomBaseUrl() throws Exception {
        sendGrid = SendGrid.create(API_KEY).setBaseUrl(BASE_URL).build();

        assertThat(sendGrid, notNullValue());
        assertThat(sendGrid.getBaseUrl(), equalTo(BASE_URL));
    }
}
