package com.revinate.sendgrid.model;

import java.util.Date;
import java.util.List;

public class Ip extends SendGridResource {

    private String ip;
    private String rdns;
    private List<String> subusers;
    private List<String> pools;
    private Date startDate;
    private Boolean warmup;
    private Boolean whitelabeled;

    @Override
    public String getPathId() {
        return ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRdns() {
        return rdns;
    }

    public void setRdns(String rdns) {
        this.rdns = rdns;
    }

    public List<String> getSubusers() {
        return subusers;
    }

    public void setSubusers(List<String> subusers) {
        this.subusers = subusers;
    }

    public List<String> getPools() {
        return pools;
    }

    public void setPools(List<String> pools) {
        this.pools = pools;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getWarmup() {
        return warmup;
    }

    public void setWarmup(Boolean warmup) {
        this.warmup = warmup;
    }

    public Boolean getWhitelabeled() {
        return whitelabeled;
    }

    public void setWhitelabeled(Boolean whitelabeled) {
        this.whitelabeled = whitelabeled;
    }
}
