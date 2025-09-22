package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmojiPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_emojis")
    @Expose
    private Integer totalEmojis;
    @SerializedName("total_like")
    @Expose
    private int total_like;
    @SerializedName("like_status")
    @Expose
    private int like_status;

    @SerializedName("bokay_status")
    @Expose
    private int bokay_status;

    @SerializedName("total_bokay")
    @Expose
    private int total_bokay;
    public int getTotal_bokay() {
        return total_bokay;
    }

    public void setTotal_bokay(int total_bokay) {
        this.total_bokay = total_bokay;}
    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getBokay_status() {
        return bokay_status;
    }

    public void setBokay_status(int bokay_status) {
        this.bokay_status = bokay_status;}
    public String getLikestatus() {
        return likestatus;
    }

    public void setLikestatus(String likestatus) {
        this.likestatus = likestatus;
    }

    public String getEmojiunicode() {
        return emojiunicode;
    }

    public void setEmojiunicode(String emojiunicode) {
        this.emojiunicode = emojiunicode;
    }

    @SerializedName("likestatus")
    @Expose
    private String likestatus;

    @SerializedName("emojiunicode")
    @Expose
    private String emojiunicode;

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

    public Integer getTotalEmojis() {
        return totalEmojis;
    }

    public void setTotalEmojis(Integer totalEmojis) {
        this.totalEmojis = totalEmojis;
    }

    public int getTotal_like() {
        return total_like;
    }

    public void setTotal_like(int total_like) {
        this.total_like = total_like;
    }
}

