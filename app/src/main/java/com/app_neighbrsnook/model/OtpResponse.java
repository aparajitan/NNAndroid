package com.app_neighbrsnook.model;

public class OtpResponse {
    private boolean status;
    private int code;
    private String message;
    private String request_id;

    // Getter and Setter for status
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    // Getter and Setter for code
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    // Getter and Setter for message
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and Setter for request_id
    public String getRequest_id() {
        return request_id;
    }
    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }
}

