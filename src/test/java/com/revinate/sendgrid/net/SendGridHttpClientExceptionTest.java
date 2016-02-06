package com.revinate.sendgrid.net;

import com.revinate.sendgrid.exception.ApiException;
import com.revinate.sendgrid.exception.AuthenticationException;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.exception.ResourceNotFoundException;
import com.revinate.sendgrid.net.auth.ApiKeyCredential;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Parameterized.class)
public class SendGridHttpClientExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {400, InvalidRequestException.class},
                {401, AuthenticationException.class},
                {404, ResourceNotFoundException.class},
                {503, ApiException.class},
        });
    }

    @Mock
    HttpClient httpClient;

    @Mock
    StringResponseHandler handler;

    SendGridHttpClient client;

    int statusCode;
    Class<? extends Throwable> expectedType;

    public SendGridHttpClientExceptionTest(int statusCode, Class<? extends Throwable> expectedType) {
        this.statusCode = statusCode;
        this.expectedType = expectedType;
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        client = new SendGridHttpClient(httpClient, handler);
    }

    @Test
    public void get_shouldHandleError() throws Exception {
        when(httpClient.execute(any(HttpGet.class), any(StringResponseHandler.class)))
                .thenThrow(new HttpResponseException(statusCode, "error"));

        thrown.expect(expectedType);
        thrown.expectMessage("error");

        client.get("http://sendgrid", new ApiKeyCredential("changeme"));
    }
}
