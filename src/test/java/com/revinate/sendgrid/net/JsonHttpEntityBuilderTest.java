package com.revinate.sendgrid.net;

import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.util.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JsonHttpEntityBuilderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    JsonHttpEntityBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new JsonHttpEntityBuilder();
    }

    @Test
    public void create_shouldCreateInstance() throws Exception {
        HttpEntityBuilder builder = HttpEntityBuilder.create(RequestType.JSON);

        assertThat(builder, notNullValue());
        assertThat(builder, instanceOf(JsonHttpEntityBuilder.class));
    }

    @Test
    public void builder_shouldAcceptEmail() throws Exception {
        Email email = new Email();
        email.setFrom("test1@email.com");
        email.addTo("test1@email.com");
        email.setText("test");

        HttpEntity entity = builder.setEmail(email).build();

        assertThat(entity, notNullValue());
        assertThat(EntityUtils.toString(entity), equalTo(JsonUtils.toJson(email)));
    }

    @Test
    public void builder_shouldAcceptModel() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("test");

        HttpEntity entity = builder.setModel(apiKey).build();

        assertThat(entity, notNullValue());
        assertThat(EntityUtils.toString(entity), equalTo(JsonUtils.toJson(apiKey)));
    }

    @Test
    public void builder_shouldAcceptMap() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("api_key", "token");

        HttpEntity entity = builder.setMap(map).build();

        assertThat(entity, notNullValue());
        assertThat(EntityUtils.toString(entity), equalTo(JsonUtils.toJson(map)));
    }

    @Test
    public void builder_shouldAcceptList() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("127.0.0.1");

        HttpEntity entity = builder.setList(list).build();

        assertThat(entity, notNullValue());
        assertThat(EntityUtils.toString(entity), equalTo(JsonUtils.toJson(list)));
    }

    @Test
    public void builder_shouldHandleNoContent() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Content is null");

        builder.build();
    }

    @Test
    public void getHeaders_shouldGenerateContentType() throws Exception {
        assertThat(builder.getHeaders(), contains(allOf(
                hasProperty("name", equalTo("Content-Type")),
                hasProperty("value", equalTo("application/json"))
        )));
    }
}