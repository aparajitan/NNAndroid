package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.SerializedName;

public class PostImage {
    @SerializedName("img")
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
