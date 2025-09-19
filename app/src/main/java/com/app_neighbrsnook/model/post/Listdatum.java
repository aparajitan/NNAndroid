package com.app_neighbrsnook.model.post;

import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Listdatum {
    @SerializedName("postid")
    @Expose
    private String postid;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("neighborhood")
    @Expose
    private String neighborhood;
    @SerializedName("post_images")
    @Expose
    private List<ImagePojo> postImages;
    @SerializedName("post_message")
    @Expose
    private String postMessage;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("emoji_status")
    @Expose
    private String emojiStatus;
    @SerializedName("user_emoji")
    @Expose
    private String userEmoji;
    @SerializedName("total_emojis")
    @Expose
    private Integer totalEmojis;
    @SerializedName("emojilistdata")
    @Expose
    private List<com.app_neighbrsnook.model.wall.Emojilistdatum> emojilistdata;
    @SerializedName("postlike")
    @Expose
    private String postlike;
    @SerializedName("totcomment")
    @Expose
    private String totcomment;
    @SerializedName("totallike")
    @Expose
    private Integer totallike;
    @SerializedName("favouritstatus")
    @Expose
    private Integer favouritstatus;
    @SerializedName("createdby")
    @Expose
    private String createdby;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    public String getPostemojiunicode() {
        return postemojiunicode;
    }

    public void setPostemojiunicode(String postemojiunicode) {
        this.postemojiunicode = postemojiunicode;
    }

    @SerializedName("postemojiunicode")
    private String  postemojiunicode;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public List<ImagePojo> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<ImagePojo> postImages) {
        this.postImages = postImages;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmojiStatus() {
        return emojiStatus;
    }

    public void setEmojiStatus(String emojiStatus) {
        this.emojiStatus = emojiStatus;
    }

    public String getUserEmoji() {
        return userEmoji;
    }

    public void setUserEmoji(String userEmoji) {
        this.userEmoji = userEmoji;
    }

    public Integer getTotalEmojis() {
        return totalEmojis;
    }

    public void setTotalEmojis(Integer totalEmojis) {
        this.totalEmojis = totalEmojis;
    }

    public List<Emojilistdatum> getEmojilistdata() {
        return emojilistdata;
    }

    public void setEmojilistdata(List<Emojilistdatum> emojilistdata) {
        this.emojilistdata = emojilistdata;
    }

    public String getPostlike() {
        return postlike;
    }

    public void setPostlike(String postlike) {
        this.postlike = postlike;
    }

    public String getTotcomment() {
        return totcomment;
    }

    public void setTotcomment(String totcomment) {
        this.totcomment = totcomment;
    }

    public Integer getTotallike() {
        return totallike;
    }

    public void setTotallike(Integer totallike) {
        this.totallike = totallike;
    }

    public Integer getFavouritstatus() {
        return favouritstatus;
    }

    public void setFavouritstatus(Integer favouritstatus) {
        this.favouritstatus = favouritstatus;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
