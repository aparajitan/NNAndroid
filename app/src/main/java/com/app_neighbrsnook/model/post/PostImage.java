package com.app_neighbrsnook.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostImage {
    @SerializedName("img")
    @Expose
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
