package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsImagesArrayShowMlt {

    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("imgid")
    @Expose
    private int imgid;
    @SerializedName("type")
    @Expose
    private String type;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
