package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetailsUserListPojo {


    @SerializedName("e_id")
    @Expose
    private String eId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("neigh")
    @Expose
    private String neigh;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("img")
    @Expose
    private String img;

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeigh() {
        return neigh;
    }

    public void setNeigh(String neigh) {
        this.neigh = neigh;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
