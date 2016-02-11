package com.revinate.sendgrid.net;

import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.Email;
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
    public void builder_shouldCreateInstance() throws Exception {
        builder = JsonHttpEntityBuilder.create();

        assertThat(builder, notNullValue());
    }

    @Test
    public void builder_shouldAcceptEmail() throws Exception {
        Email email = new Email();
        email.setFrom("test1@email.com");
        email.addTo("test1@email.com");
        email.setText("test");
        HttpEntity entity = builder.setContent(email).build();

        assertThat(entity, notNullValue());
        assertThat(EntityUtils.toString(entity), equalTo(JsonUtils.toJson(email)));
    }

    @Test
    public void builder_shouldAcceptModel() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("test");
        HttpEntity entity = builder.setContent(apiKey).build();

        assertThat(entity, notNullValue());
        assertThat(EntityUtils.toString(entity), equalTo(JsonUtils.toJson(apiKey)));
    }

    @Test
    public void builder_shouldHandleContent() throws Exception {
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