package com.revinate.sendgrid.model;

public class DnsRecordValidation extends SendGridModel {

    private Boolean valid;
    private String reason;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
