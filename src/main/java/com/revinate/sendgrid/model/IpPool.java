package com.revinate.sendgrid.model;

import java.util.List;

public class IpPool extends SendGridResource {

    private String name;
    private String poolName;
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

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public List<Ip> getIps() {
        return ips;
    }

    public void setIps(List<Ip> ips) {
        this.ips = ips;
    }
}
