package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.Account;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class AccountResource extends SingularEntityResource<Account> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "user/account";

    public AccountResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, Account.class);
    }

    @Override
    public Account create(Account account) throws SendGridException {
        throw unsupported();
    }

    @Override
    public Account update(Account account) throws SendGridException {
        throw unsupported();
    }

    @Override
    public void delete() throws SendGridException {
        throw unsupported();
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
