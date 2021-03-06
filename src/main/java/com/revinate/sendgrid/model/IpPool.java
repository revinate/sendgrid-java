package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class IpPool extends SendGridModel implements SendGridEntity {

    private String name;
    private List<Ip> ips;

    public IpPool() {
        // no args constructor for Jackson
    }

    public IpPool(String name) {
        this.name = name;
    }

    @Override
    public String getEntityId() {
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

    public void addIp(Ip ip) {
        if (ips == null) {
            ips = new ArrayList<Ip>();
        }
        ips.add(ip);
    }
}
