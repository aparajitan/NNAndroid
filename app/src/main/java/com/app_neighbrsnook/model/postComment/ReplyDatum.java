package com.app_neighbrsnook.model.postComment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReplyDatum {

    @SerializedName("pc_id")
    @Expose
    private String pcId;

    @SerializedName("postid")
    @Expose
    private String postid;

    @SerializedName("userid")
    @Expose
    private String userid;

    @SerializedName("commenttext")
    @Expose
    private String commenttext;

    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("createon")
    @Expose
    private String createon;

    @SerializedName("userpic")
    @Expose
    private String userpic;

    @SerializedName("top_level_username")
    @Expose
    private String top_level_username;

    @SerializedName("top_level_userid")
    @Expose
    private String top_level_userid;

    @SerializedName("total_likes")
    @Expose
    private String total_likes;

    @SerializedName("user_like_status")
    @Expose
    private String user_like_status;

    @SerializedName("total_comments")
    @Expose
    private String total_comments;

    @SerializedName("replies")
    @Expose
    private List<ReplyDatum> replies;

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCommenttext() {
        return commenttext;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getTop_level_username() {
        return top_level_username;
    }

    public void setTop_level_username(String top_level_username) {
        this.top_level_username = top_level_username;
    }

    public String getTop_level_userid() {
        return top_level_userid;
    }

    public void setTop_level_userid(String top_level_userid) {
        this.top_level_userid = top_level_userid;
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

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public List<ReplyDatum> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyDatum> replies) {
        this.replies = replies;
    }
}
