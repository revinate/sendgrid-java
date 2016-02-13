package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class Subuser extends SendGridModel implements SendGridEntity {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Boolean disabled;
    private String signupSessionToken;
    private String authorizationToken;
    private CreditAllocation creditAllocation;
    private List<String> ips;

    public Subuser() {
        // no args constructor for Jackson
    }

    public Subuser(String username) {
        this.username = username;
    }

    public Subuser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getEntityId() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getSignupSessionToken() {
        return signupSessionToken;
    }

    public void setSignupSessionToken(String signupSessionToken) {
        this.signupSessionToken = signupSessionToken;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public CreditAllocation getCreditAllocation() {
        return creditAllocation;
    }

    public void setCreditAllocation(CreditAllocation creditAllocation) {
        this.creditAllocation = creditAllocation;
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public void addIp(String ip) {
        if (ips == null) {
            ips = new ArrayList<String>();
        }
        ips.add(ip);
    }
}
