package com.revinate.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identifiable {

    @JsonIgnore
    String getPathId();
}
