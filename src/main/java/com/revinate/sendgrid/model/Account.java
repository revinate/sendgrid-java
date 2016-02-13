package com.revinate.sendgrid.model;

public class Account extends SendGridModel {

    private String type;
    private Double reputation;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getReputation() {
        return reputation;
    }

    public void setReputation(Double reputation) {
        this.reputation = reputation;
    }
}
