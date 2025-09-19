package com.app_neighbrsnook.model.postComment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentLikePojo {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("total_likes")
    @Expose
    private String total_likes;

    @SerializedName("user_like_status")
    @Expose
    private String user_like_status;

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

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    public String getUser_like_status() {
        return user_like_status;
    }

    public void setUser_like_status(String user_like_status) {
        this.user_like_status = user_like_status;
    }
}
