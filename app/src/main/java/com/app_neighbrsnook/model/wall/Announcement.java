package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.SerializedName;

public class Announcement {
    @SerializedName("title")
    private String type;
    @SerializedName("msg")

    private String msg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
