package com.revinate.sendgrid.model;

public class MailSetting extends SendGridModel implements SendGridEntity {

    private String name;
    private String title;
    private String description;
    private Boolean enabled;
    private String email;

    public MailSetting() {
        // no args constructor for Jackson
    }

    public MailSetting(String name, Boolean enabled, String email) {
        this.name = name;
        this.enabled = enabled;
        this.email = email;
    }

    @Override
    public String getEntityId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
