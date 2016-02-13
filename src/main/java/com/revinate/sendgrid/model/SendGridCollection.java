package com.revinate.sendgrid.model;

import java.util.List;

public interface SendGridCollection<T extends SendGridEntity> {

    List<T> getData();
}
