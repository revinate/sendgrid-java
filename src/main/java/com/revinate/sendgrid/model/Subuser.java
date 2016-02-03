package com.revinate.sendgrid.model;

import java.util.ArrayList;
import java.util.List;

public class Subuser extends SendGridResource {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private List<String> ips;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<String> getIps() {
        if (ips == null) {
            ips = new ArrayList<String>();
        }
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public void addIp(String ip) {
        getIps().add(ip);
    }
}
