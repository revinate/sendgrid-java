package com.revinate.sendgrid.net;

import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import org.apache.http.HttpEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MultipartHttpEntityBuilderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    MultipartHttpEntityBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new MultipartHttpEntityBuilder();
    }

    @Test
    public void create_shouldCreateInstance() throws Exception {
        HttpEntityBuilder builder = HttpEntityBuilder.create(RequestType.MULTIPART);

        assertThat(builder, notNullValue());
        assertThat(builder, instanceOf(MultipartHttpEntityBuilder.class));
    }

    @Test
    public void builder_shouldAcceptCredential() throws Exception {
        Email email = new Email();
        email.setFrom("test1@email.com");
        email.addTo("test1@email.com");
        email.setText("test");
        Credential credential = new UsernamePasswordCredential("username", "password");

        HttpEntity entity = builder.setCredential(credential).setEmail(email).build();

        assertThat(entity, notNullValue());
    }

    @Test
    public void builder_shouldAcceptEmail() throws Exception {
        Email email = new Email();
        email.setFrom("test1@email.com");
        email.addTo("test1@email.com");
        email.setText("test");

        HttpEntity entity = builder.setEmail(email).build();

        assertThat(entity, notNullValue());
    }

    @Test
    public void builder_shouldAcceptMap() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("api_key", "token");

        HttpEntity entity = builder.setMap(map).build();

        assertThat(entity, notNullValue());
    }

    @Test
    public void builder_shouldNotAcceptModel() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("test");

        thrown.expect(IOException.class);
        thrown.expectMessage("Content is of unsupported type");

        builder.setModel(apiKey).build();
    }

    @Test
    public void builder_shouldNotAcceptList() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("127.0.0.1");

        thrown.expect(IOException.class);
        thrown.expectMessage("Content is of unsupported type");

        builder.setList(list).build();
    }

    @Test
    public void builder_shouldHandleNoContent() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Content is null");

        builder.build();
    }
}