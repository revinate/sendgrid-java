package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class IpWhitelabelCollection extends ArrayList<IpWhitelabel> implements SendGridCollection<IpWhitelabel> {

    @Override
    public List<IpWhitelabel> getData() {
        return this;
    }
}
