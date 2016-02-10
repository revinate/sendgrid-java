package com.revinate.sendgrid.model;

public class Monitor extends SendGridModel {

    private String email;
    private Integer frequency;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
