package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class IpPoolCollection extends ArrayList<IpPool> implements SendGridCollection<IpPool> {

    @Override
    public List<IpPool> getData() {
        return this;
    }
}
