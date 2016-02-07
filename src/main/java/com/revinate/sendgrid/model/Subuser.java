package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class Subuser extends SendGridResource implements Identifiable {

    private Integer id;
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String signupSessionToken;
    private String authorizationToken;
    private CreditAllocation creditAllocation;
    private List<String> ips;

    @Override
    public String getPathId() {
        return username;
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
