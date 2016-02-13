package com.revinate.sendgrid.model;

import java.util.Map;

public class WhitelabelValidation extends SendGridModel implements SendGridEntity {

    private Integer id;
    private Boolean valid;
    private Map<String, DnsRecordValidation> validationResults;

    @Override
    public String getEntityId() {
        return id.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Map<String, DnsRecordValidation> getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(Map<String, DnsRecordValidation> validationResults) {
        this.validationResults = validationResults;
    }
}
