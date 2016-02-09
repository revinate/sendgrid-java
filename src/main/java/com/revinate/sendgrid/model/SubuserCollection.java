package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class SubuserCollection extends ArrayList<Subuser> implements SendGridCollection<Subuser> {

    @Override
    public List<Subuser> getData() {
        return this;
    }
}
