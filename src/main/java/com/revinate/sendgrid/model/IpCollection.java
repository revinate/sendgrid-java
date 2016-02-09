package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class IpCollection extends ArrayList<Ip> implements SendGridCollection<Ip> {

    @Override
    public List<Ip> getData() {
        return this;
    }
}
