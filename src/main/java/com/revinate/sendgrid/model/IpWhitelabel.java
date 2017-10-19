package com.revinate.sendgrid.model;

import java.util.List;

public class IpWhitelabel extends SendGridModel implements SendGridEntity {

    private Integer id;
    private String ip;
    private String rdns;
    private String domain;
    private String subdomain;
    private Boolean valid;
    private Boolean legacy;
    private DnsRecord aRecord;
    private List<User> users;

    public IpWhitelabel() {
        // no args constructor for Jackson
    }

    public IpWhitelabel(String domain, String subdomain, String ip) {
        this.domain = domain;
        this.subdomain = subdomain;
        this.ip = ip;
    }

    @Override
    public String getEntityId() {
        return id.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getLegacy() {
        return legacy;
    }

    public void setLegacy(Boolean legacy) {
        this.legacy = legacy;
    }

    public DnsRecord getaRecord() {
        return aRecord;
    }

    public void setaRecord(DnsRecord aRecord) {
        this.aRecord = aRecord;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
