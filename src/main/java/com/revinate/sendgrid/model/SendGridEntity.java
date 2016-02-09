package com.revinate.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface SendGridEntity {

    @JsonIgnore
    String getEntityId();
}
