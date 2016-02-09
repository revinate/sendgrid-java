package com.revinate.sendgrid.model;

import java.util.List;

public interface SendGridCollection<T extends SendGridEntity> {

    public List<T> getData();
}
