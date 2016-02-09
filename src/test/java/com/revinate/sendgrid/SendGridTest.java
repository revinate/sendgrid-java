package com.revinate.sendgrid;

import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.net.auth.OnBehalfOfCredential;
import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import com.revinate.sendgrid.operations.ApiKeyOperations;
import com.revinate.sendgrid.operations.IpOperations;
import com.revinate.sendgrid.operations.IpPoolOperations;
import com.revinate.sendgrid.operations.SubuserOperations;
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
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SendGridTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String API_KEY = "token";
    private static final String URL = "http://sendgrid";
    private static final int MAX_CONNECTIONS = 50;

    @Mock
    SendGridHttpClient client;

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
    public void usernamePasswordConstructor_shouldConstructInstance() throws Exception {
        SendGrid sendGrid = new SendGrid(USERNAME, PASSWORD);

        Credential credential = sendGrid.getCredential();
        assertThat(credential, instanceOf(UsernamePasswordCredential.class));

        UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
        assertThat(usernamePasswordCredential.getUsername(), equalTo(USERNAME));
        assertThat(usernamePasswordCredential.getPassword(), equalTo(PASSWORD));
    }

    @Test
    public void apiKeyConstructor_shouldConstructInstance() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY);

        Credential credential = sendGrid.getCredential();
        assertThat(credential, instanceOf(ApiKeyCredential.class));

        ApiKeyCredential apiKeyCredential = (ApiKeyCredential) credential;
        assertThat(apiKeyCredential.getApiKey(), equalTo(API_KEY));
    }

    @Test
    public void maxConnectionsConstructor_shouldConstructInstance() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY, MAX_CONNECTIONS);

        Credential credential = sendGrid.getCredential();
        assertThat(credential, instanceOf(ApiKeyCredential.class));

        ApiKeyCredential apiKeyCredential = (ApiKeyCredential) credential;
        assertThat(apiKeyCredential.getApiKey(), equalTo(API_KEY));
    }

    @Test
    public void onBehalfOf_shouldOverlayCredential() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY).setUrl(URL).setClient(client);

        ApiKeyOperations operations = sendGrid.onBehalfOf("username2").apiKeys();

        assertThat(operations, notNullValue());
        assertThat(operations.getBaseUrl(), equalTo(URL));
        assertThat(operations.getClient(), sameInstance(client));

        Credential credential = operations.getCredential();
        assertThat(credential, notNullValue());
        assertThat(credential, instanceOf(OnBehalfOfCredential.class));

        OnBehalfOfCredential onBehalfOfCredential = (OnBehalfOfCredential) credential;
        assertThat(onBehalfOfCredential.getCredential(), sameInstance(sendGrid.getCredential()));
        assertThat(onBehalfOfCredential.getUsername(), equalTo("username2"));
    }

    @Test
    public void apiKeys_shouldReturnOperations() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY).setUrl(URL).setClient(client);

        ApiKeyOperations operations = sendGrid.apiKeys();

        assertThat(operations, notNullValue());
        assertThat(operations.getBaseUrl(), equalTo(URL));
        assertThat(operations.getClient(), sameInstance(client));
        assertThat(operations.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void subusers_shouldReturnOperations() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY).setUrl(URL).setClient(client);

        SubuserOperations operations = sendGrid.subusers();

        assertThat(operations, notNullValue());
        assertThat(operations.getBaseUrl(), equalTo(URL));
        assertThat(operations.getClient(), sameInstance(client));
        assertThat(operations.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void ips_shouldReturnOperations() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY).setUrl(URL).setClient(client);

        IpOperations operations = sendGrid.ips();

        assertThat(operations, notNullValue());
        assertThat(operations.getBaseUrl(), equalTo(URL));
        assertThat(operations.getClient(), sameInstance(client));
        assertThat(operations.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void ipPools_shouldReturnOperations() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY).setUrl(URL).setClient(client);

        IpPoolOperations operations = sendGrid.ipPools();

        assertThat(operations, notNullValue());
        assertThat(operations.getBaseUrl(), equalTo(URL));
        assertThat(operations.getClient(), sameInstance(client));
        assertThat(operations.getCredential(), sameInstance(sendGrid.getCredential()));
    }

    @Test
    public void close_shouldCloseUnderlyingClient() throws Exception {
        SendGrid sendGrid = new SendGrid(API_KEY).setUrl(URL).setClient(client);

        sendGrid.close();

        verify(client).close();
    }
}
