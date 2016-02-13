package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.MailSetting;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
import com.revinate.sendgrid.net.auth.Credential;

public class MailSettingResource extends EntityResource<MailSetting> {

    public MailSettingResource(String baseUrl, SendGridHttpClient client, Credential credential, MailSetting setting) {
        super(baseUrl, client, credential, MailSetting.class, setting);
    }

    public MailSettingResource(String baseUrl, SendGridHttpClient client, Credential credential, String id) {
        super(baseUrl, client, credential, MailSetting.class, id);
    }

    @Override
    public MailSetting update(MailSetting setting) throws SendGridException {
        return client.patch(getUrl(), MailSetting.class, credential, setting, RequestType.JSON);
    }

    @Override
    public void delete() throws SendGridException {
        throw unsupported();
    }
}
