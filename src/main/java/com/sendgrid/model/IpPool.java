package com.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IpPool extends SendGridResource {

    private String name;
    @JsonProperty("pool_name")
    private String poolName;
    private List<Ip> ips;

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
