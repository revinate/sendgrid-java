package com.revinate.sendgrid.model;

import java.util.List;
import java.util.Map;

public class Whitelabel extends SendGridModel implements SendGridEntity {

    private Integer id;
    private Integer userId;
    private String username;
    private String domain;
    private String subdomain;
    private List<String> ips;
    private Boolean automaticSecurity;
    private Boolean customSpf;
    private Boolean isDefault;
    private Boolean legacy;
    private Boolean valid;
    private Map<String, DnsRecord> dns;

    public Whitelabel() {
        // no args constructor for Jackson
    }

    public Whitelabel(String domain, String subdomain) {
        this.domain = domain;
        this.subdomain = subdomain;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public Boolean getAutomaticSecurity() {
        return automaticSecurity;
    }

    public void setAutomaticSecurity(Boolean automaticSecurity) {
        this.automaticSecurity = automaticSecurity;
    }

    public Boolean getCustomSpf() {
        return customSpf;
    }

    public void setCustomSpf(Boolean customSpf) {
        this.customSpf = customSpf;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getLegacy() {
        return legacy;
    }

    public void setLegacy(Boolean legacy) {
        this.legacy = legacy;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Map<String, DnsRecord> getDns() {
        return dns;
    }

    public void setDns(Map<String, DnsRecord> dns) {
        this.dns = dns;
    }
}
