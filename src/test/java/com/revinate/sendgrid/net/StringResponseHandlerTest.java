package com.revinate.sendgrid.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StringResponseHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    StringResponseHandler.HttpEntityReader reader;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    HttpResponse httpResponse;

    StringResponseHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new StringResponseHandler(reader);
    }

    @Test
    public void handleResponse_shouldHandleSuccessfulResponse() throws Exception {
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(reader.readContent(httpResponse.getEntity())).thenReturn("body");

        String actual = handler.handleResponse(httpResponse);

        assertThat(actual, is("body"));
    }

    @Test
    public void handleResponse_shouldHandleEmptyResponse() throws Exception {
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(204);
        when(httpResponse.getEntity()).thenReturn(null);

        thrown.expect(ClientProtocolException.class);

        handler.handleResponse(httpResponse);
    }

    @Test
    public void handleResponse_shouldHandleErrorResponse() throws Exception {
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(404);
        when(reader.readContent(httpResponse.getEntity())).thenReturn("not found");

        thrown.expect(HttpResponseException.class);

        handler.handleResponse(httpResponse);
    }
}
