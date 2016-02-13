package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.exception.InvalidRequestException;
import com.revinate.sendgrid.model.Account;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    AccountResource resource;


    @Before
    public void setUp() throws Exception {
        resource = new AccountResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void retrieve_shouldReturnAccount() throws Exception {
        Account response = JsonUtils.fromJson(readFile("/responses/account.json"), Account.class);

        when(client.get("https://api.sendgrid.com/v3/user/account",
                Account.class, credential)).thenReturn(response);

        Account account = resource.retrieve();

        assertThat(account, sameInstance(response));
    }

    @Test
    public void delete_shouldThrowUnsupported() throws Exception {
        thrown.expect(InvalidRequestException.class);
        thrown.expectMessage("Operation not supported on this resource");

        resource.delete();
    }
}
