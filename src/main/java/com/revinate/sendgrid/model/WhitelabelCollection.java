package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class WhitelabelCollection extends ArrayList<Whitelabel> implements SendGridCollection<Whitelabel> {

    @Override
    public List<Whitelabel> getData() {
        return this;
    }
}
