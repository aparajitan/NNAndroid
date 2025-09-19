package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Poll {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    @SerializedName("verfied_msg")
    private String verfied_msg;

    @SerializedName("nbdata")
    private List<PollListPojo> nbdata;

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

    public String getVerfied_msg() {
        return verfied_msg;
    }

    public void setVerfied_msg(String verfied_msg) {
        this.verfied_msg = verfied_msg;
    }

    public List<PollListPojo> getNbdata() {
        return nbdata;
    }

    public void setNbdata(List<PollListPojo> nbdata) {
        this.nbdata = nbdata;
    }
}
