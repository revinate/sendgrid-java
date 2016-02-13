package com.revinate.sendgrid.model;

import java.util.List;

public class MailSettingsResponse extends SendGridModel implements SendGridCollection<MailSetting> {

    private List<MailSetting> result;

    @Override
    public List<MailSetting> getData() {
        return result;
    }

    public List<MailSetting> getResult() {
        return result;
    }

    public void setResult(List<MailSetting> result) {
        this.result = result;
    }
}
