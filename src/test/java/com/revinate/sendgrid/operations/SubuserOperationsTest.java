package com.revinate.sendgrid.operations;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Subuser;
import com.revinate.sendgrid.model.SubuserCollection;
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
public class SubuserOperationsTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    SubuserOperations operations;

    @Before
    public void setUp() throws Exception {
        operations = new SubuserOperations("https://api.sendgrid.com", client, credential);
    }

    @Test
    public void list_shouldReturnSubusers() throws Exception {
        SubuserCollection response = JsonUtils.fromJson(readFile("/responses/subusers.json"),
                SubuserCollection.class);

        when(client.get("https://api.sendgrid.com/v3/subusers", SubuserCollection.class, credential))
                .thenReturn(response);

        List<Subuser> subusers = operations.list();

        assertThat(subusers, sameInstance(response.getData()));
    }

    @Test
    public void retrieve_shouldReturnSubuser() throws Exception {
        Subuser response = JsonUtils.fromJson(readFile("/responses/subuser.json"), Subuser.class);

        when(client.get("https://api.sendgrid.com/v3/subusers/" + response.getUsername(),
                Subuser.class, credential)).thenReturn(response);

        Subuser subuser = operations.retrieve(response.getUsername());

        assertThat(subuser, sameInstance(response));
    }

    @Test
    public void create_shouldPostAndReturnSubuser() throws Exception {
        Subuser response = JsonUtils.fromJson(readFile("/responses/subuser.json"), Subuser.class);
        Subuser subuser = new Subuser();
        subuser.setUsername("test1");
        subuser.setEmail("test1@email.com");
        subuser.setPassword("testpassword");
        subuser.addIp("127.0.0.1");

        when(client.post("https://api.sendgrid.com/v3/subusers", subuser,
                Subuser.class, credential)).thenReturn(response);

        Subuser subuser1 = operations.create(subuser);

        assertThat(subuser1, sameInstance(response));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPutIpsAndReturnSubuser() throws Exception {
        Subuser response = JsonUtils.fromJson(readFile("/responses/subuser-ips.json"), Subuser.class);
        Subuser subuser = new Subuser();
        subuser.setUsername("test1");
        subuser.setEmail("test1@email.com");
        subuser.addIp("127.0.0.1");

        when(client.put(any(String.class), any(Subuser.class), any(Class.class),
                any(Credential.class))).thenReturn(response);

        Subuser subuser1 = operations.update(subuser);

        assertThat(subuser1, notNullValue());
        assertThat(subuser1.getUsername(), equalTo(subuser.getUsername()));
        assertThat(subuser1.getEmail(), equalTo(subuser.getEmail()));
        assertThat(subuser1.getIps(), contains("127.0.0.1"));

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(client).put(eq("https://api.sendgrid.com/v3/subusers/" + response.getUsername() + "/ips"),
                captor.capture(), eq(Subuser.class), eq(credential));

        List<String> ips = captor.getValue();

        assertThat(ips, contains("127.0.0.1"));
    }

    @Test
    public void update_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setEmail("test1@email.com");

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Resource missing identifier");

        operations.update(subuser);
    }

    @Test
    public void partialUpdate_shouldPatchAndReturnSubuser() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setUsername("test1");
        subuser.setEmail("test1@email.com");
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("disabled", true);

        Subuser subuser1 = operations.partialUpdate(subuser, requestObject);

        verify(client).patch("https://api.sendgrid.com/v3/subusers/" + subuser.getUsername(), requestObject, credential);
        assertThat(subuser1, notNullValue());
        assertThat(subuser1.getUsername(), equalTo(subuser.getUsername()));
        assertThat(subuser1.getEmail(), equalTo(subuser.getEmail()));
        assertThat(subuser1.getDisabled(), equalTo(true));
    }

    @Test
    public void partialUpdate_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setEmail("test1@email.com");
        Map<String, Object> requestObject = new HashMap<String, Object>();
        requestObject.put("disabled", true);

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Resource missing identifier");

        operations.partialUpdate(subuser, requestObject);
    }

    @Test
    public void delete_shouldDeleteSubuser() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setUsername("test1");
        subuser.setEmail("test1@email.com");

        operations.delete(subuser);

        verify(client).delete("https://api.sendgrid.com/v3/subusers/" + subuser.getUsername(), credential);
    }

    @Test
    public void delete_shouldHandleMissingId() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setEmail("test1@email.com");

        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Resource missing identifier");

        operations.delete(subuser);
    }
}
