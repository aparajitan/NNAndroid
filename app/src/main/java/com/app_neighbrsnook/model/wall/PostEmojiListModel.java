package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostEmojiListModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    @SerializedName("total_emojis")
    private String total_emojis;

    @SerializedName("listdata")
    private List<Emojilistdatum> emojilistdata;

    @SerializedName("like_list")
    private List<Emojilistdatum>like_list;

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

    public String getTotal_emojis() {
        return total_emojis;
    }

    public void setTotal_emojis(String total_emojis) {
        this.total_emojis = total_emojis;
    }

    public List<Emojilistdatum> getEmojilistdata() {
        return emojilistdata;
    }

    public void setEmojilistdata(List<Emojilistdatum> emojilistdata) {
        this.emojilistdata = emojilistdata;
    }
    public List<Emojilistdatum> getLike_list() {
        return like_list;
    }

    public void setLike_list(List<Emojilistdatum> like_list) {
        this.like_list = like_list;
}
}
