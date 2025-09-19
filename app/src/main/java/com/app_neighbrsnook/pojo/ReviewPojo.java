package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewPojo {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("listdata")
    private List<Reviewdatum> listdata;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Reviewdatum> getListdata() {
        return listdata;
    }

    public void setListdata(List<Reviewdatum> listdata) {
        this.listdata = listdata;
    }
}
