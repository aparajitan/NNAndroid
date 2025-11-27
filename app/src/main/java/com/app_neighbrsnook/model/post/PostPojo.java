package com.app_neighbrsnook.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("verfied_msg")
    @Expose
    private String verfiedMsg;
    @SerializedName("listdata")
    @Expose
    private List<Listdatum> listdata;

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

    public String getVerfiedMsg() {
        return verfiedMsg;
    }

    public void setVerfiedMsg(String verfiedMsg) {
        this.verfiedMsg = verfiedMsg;
    }

    public List<Listdatum> getListdata() {
        return listdata;
    }

    public void setListdata(List<Listdatum> listdata) {
        this.listdata = listdata;
    }
}
