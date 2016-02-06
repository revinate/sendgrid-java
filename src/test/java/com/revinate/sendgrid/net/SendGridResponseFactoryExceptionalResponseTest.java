package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.ApiException;
import com.revinate.sendgrid.exception.AuthenticationException;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.exception.NotFoundException;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Answers;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Parameterized.class)
public class SendGridResponseFactoryExceptionalResponseTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {400, InvalidRequestException.class},
                {401, AuthenticationException.class},
                {404, NotFoundException.class},
                {503, ApiException.class},
        });
    }

    @Mock
    SendGridResponseFactory.HttpEntityReader reader;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    HttpResponse httpResponse;

    SendGridResponseFactory factory;

    int statusCode;
    Class<? extends Throwable> expectedType;

    public SendGridResponseFactoryExceptionalResponseTest(int statusCode, Class<? extends Throwable> expectedType) {
        this.statusCode = statusCode;
        this.expectedType = expectedType;
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        factory = new SendGridResponseFactory(reader);
    }

    @Test
    public void create_shouldHandleError() throws Exception {
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(statusCode);
        when(reader.readContent(httpResponse.getEntity())).thenReturn("response message");

        SendGridResponse actual = factory.handleResponse(httpResponse);

        assertThat(actual, notNullValue());
        assertThat(actual.isSuccessful(), is(false));
        assertThat(actual.getException(), instanceOf(expectedType));
    }
}
