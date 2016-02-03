package com.revinate.sendgrid.model;

public class Response {

    private int code;
    private boolean success;
    private String message;

    public Response(int code, String msg) {
        this.code = code;
        this.success = code == 200;
        this.message = msg;
    }

    public int getCode() {
        return this.code;
    }

    public boolean getStatus() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }
}
