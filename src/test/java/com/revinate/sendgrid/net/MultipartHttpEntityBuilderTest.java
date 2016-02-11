package com.revinate.sendgrid.net;

import com.revinate.sendgrid.model.ApiKey;
import com.revinate.sendgrid.model.Email;
import com.revinate.sendgrid.net.auth.Credential;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MultipartHttpEntityBuilderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    Credential credential;

    MultipartHttpEntityBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new MultipartHttpEntityBuilder(credential);
    }

    @Test
    public void builder_shouldCreateInstance() throws Exception {
        builder = MultipartHttpEntityBuilder.create(credential);

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
    }

    @Test
    public void builder_shouldNotAcceptModel() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setName("test");
        builder.setContent(apiKey);

        thrown.expect(IOException.class);
        thrown.expectMessage("Content is of unsupported type");

        builder.build();
    }

    @Test
    public void builder_shouldHandleContent() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Content is null");

        builder.build();
    }

    @Test
    public void getHeaders_shouldBeEmpty() throws Exception {
        assertThat(builder.getHeaders(), emptyCollectionOf(Header.class));
    }

}