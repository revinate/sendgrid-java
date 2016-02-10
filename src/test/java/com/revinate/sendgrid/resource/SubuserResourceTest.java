package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Subuser;
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
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubuserResourceTest extends BaseSendGridTest {

    private static final String USERNAME = "test1";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    SubuserResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new SubuserResource("https://api.sendgrid.com/v3/subusers", client, credential, USERNAME);
    }

    @Test
    public void monitor_shouldReturnResource() throws Exception {
        MonitorResource subresource = resource.monitor();

        assertThat(subresource, notNullValue());
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/" + USERNAME));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void monitor_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        resource = new SubuserResource("https://api.sendgrid.com/v3/subusers", client, credential, subuser);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.monitor();
    }

    @Test
    public void retrieve_shouldReturnSubuser() throws Exception {
        Subuser response = JsonUtils.fromJson(readFile("/responses/subuser.json"), Subuser.class);

        when(client.get("https://api.sendgrid.com/v3/subusers/" + USERNAME,
                Subuser.class, credential)).thenReturn(response);

        Subuser subuser = resource.retrieve();

        assertThat(subuser, sameInstance(response));
    }

    @Test
    public void retrieve_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setEmail("test1@email.com");
        resource = new SubuserResource("https://api.sendgrid.com/v3/subusers", client, credential, subuser);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.retrieve();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPutIpsAndReturnSubuser() throws Exception {
        Subuser response = JsonUtils.fromJson(readFile("/responses/subuser-ips.json"), Subuser.class);
        Subuser subuser = new Subuser();
        subuser.setUsername(USERNAME);
        subuser.setEmail("test1@email.com");
        subuser.addIp("127.0.0.1");

        when(client.put(any(String.class), any(Subuser.class), any(Class.class),
                any(Credential.class))).thenReturn(response);

        Subuser subuser1 = resource.update(subuser);

        assertThat(subuser1, notNullValue());
        assertThat(subuser1.getUsername(), equalTo(USERNAME));
        assertThat(subuser1.getEmail(), equalTo(subuser.getEmail()));
        assertThat(subuser1.getIps(), contains("127.0.0.1"));

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(client).put(eq("https://api.sendgrid.com/v3/subusers/" + USERNAME + "/ips"),
                captor.capture(), eq(Subuser.class), eq(credential));

        List<String> ips = captor.getValue();

        assertThat(ips, contains("127.0.0.1"));
    }

    @Test
    public void update_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setEmail("test1@email.com");
        resource = new SubuserResource("https://api.sendgrid.com/v3/subusers", client, credential, subuser);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.update(subuser);
    }

    @Test
    public void partialUpdate_shouldPatchAndReturnSubuser() throws Exception {
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("disabled", true);

        Subuser subuser1 = resource.partialUpdate(requestObject);

        verify(client).patch("https://api.sendgrid.com/v3/subusers/" + USERNAME, requestObject, credential);

        assertThat(subuser1, notNullValue());
        assertThat(subuser1.getDisabled(), equalTo(true));
    }

    @Test
    public void partialUpdate_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setEmail("test1@email.com");
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("disabled", true);
        resource = new SubuserResource("https://api.sendgrid.com/v3/subusers", client, credential, subuser);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.partialUpdate(requestObject);
    }

    @Test
    public void delete_shouldDeleteSubuser() throws Exception {
        resource.delete();

        verify(client).delete("https://api.sendgrid.com/v3/subusers/" + USERNAME, credential);
    }

    @Test
    public void delete_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setEmail("test1@email.com");
        resource = new SubuserResource("https://api.sendgrid.com/v3/subusers", client, credential, subuser);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Missing entity identifier");

        resource.delete();
    }
}
