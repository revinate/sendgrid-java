package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.model.Subuser;
import com.revinate.sendgrid.model.SubuserCollection;
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
public class SubusersResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    SubusersResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new SubusersResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void entity_shouldReturnResource() throws Exception {
        Subuser subuser = new Subuser();
        subuser.setUsername("test");

        SubuserResource subresource = resource.entity(subuser);

        assertThat(subresource, notNullValue());
        assertThat(subresource.getId(), equalTo("test"));
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/subusers"));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void list_shouldReturnSubusers() throws Exception {
        SubuserCollection response = JsonUtils.fromJson(readFile("/responses/subusers.json"),
                SubuserCollection.class);

        when(client.get("https://api.sendgrid.com/v3/subusers", SubuserCollection.class, credential))
                .thenReturn(response);

        List<Subuser> subusers = resource.list();

        assertThat(subusers, sameInstance(response.getData()));
    }

    @Test
    public void create_shouldPostAndReturnSubuser() throws Exception {
        Subuser response = JsonUtils.fromJson(readFile("/responses/subuser.json"), Subuser.class);
        Subuser subuser = new Subuser();
        subuser.setUsername("test1");
        subuser.setEmail("test1@email.com");
        subuser.setPassword("testpassword");
        subuser.addIp("127.0.0.1");

        when(client.post("https://api.sendgrid.com/v3/subusers",
                Subuser.class, credential, subuser, RequestType.JSON)).thenReturn(response);

        Subuser subuser1 = resource.create(subuser);

        assertThat(subuser1, sameInstance(response));
    }
}
