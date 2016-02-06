package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.ApiException;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SendGridResponseFactoryTest {

    @Mock
    SendGridResponseFactory.HttpEntityReader reader;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    HttpResponse httpResponse;

    SendGridResponseFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new SendGridResponseFactory(reader);
    }

    @Test
    public void create_shouldHandleSuccessfulResponse() throws Exception {
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(reader.readContent(httpResponse.getEntity())).thenReturn("body");

        SendGridResponse actual = factory.handleResponse(httpResponse);

        assertThat(actual, notNullValue());
        assertThat(actual.isSuccessful(), is(true));
        assertThat(actual.getResponseBody(), is("body"));
        assertThat(actual.getException(), nullValue());
    }

    @Test
    public void create_shouldHandleEmptyResponse() throws Exception {
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(204);
        when(httpResponse.getEntity()).thenReturn(null);

        SendGridResponse actual = factory.handleResponse(httpResponse);

        assertThat(actual, notNullValue());
        assertThat(actual.isSuccessful(), is(false));
        assertThat(actual.getResponseBody(), nullValue());
        assertThat(actual.getException(), instanceOf(ApiException.class));
    }
}