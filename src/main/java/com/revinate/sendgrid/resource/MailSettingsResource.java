package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.exception.SendGridException;
import com.revinate.sendgrid.model.MailSetting;
import com.revinate.sendgrid.model.MailSettingsResponse;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class MailSettingsResource extends CollectionResource<MailSetting, MailSettingsResponse> {

    public static final ApiVersion API_VERSION = ApiVersion.V3;
    public static final String ENDPOINT = "mail_settings";

    public MailSettingsResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential, MailSetting.class, MailSettingsResponse.class);
    }

    public MailSettingResource entity(MailSetting setting) {
        return new MailSettingResource(getUrl(), client, credential, setting);
    }

    public MailSettingResource entity(String id) {
        return new MailSettingResource(getUrl(), client, credential, id);
    }

    @Override
    public MailSetting create(MailSetting mailSetting) throws SendGridException {
        throw unsupported();
    }

    @Override
    protected String getEndpoint() {
        return ENDPOINT;
    }
}
