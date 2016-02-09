package com.revinate.sendgrid.model;

import java.util.List;

public class IpPool extends SendGridResource implements Identifiable {

    private String name;
    private List<Ip> ips;

    @Override
    public String getPathId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoolName(String name) {
        this.name = name;
    }

    public List<Ip> getIps() {
        return ips;
    }

    public void setIps(List<Ip> ips) {
        this.ips = ips;
    }
}
