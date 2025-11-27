package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Emojilistdatum {

    @SerializedName("postid")
    @Expose
    private String postid;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userpic")
    @Expose
    private String userpic;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;
    @SerializedName("emoji")
    @Expose
    private String emoji;
    @SerializedName("createon")
    @Expose
    private String createon;

    public String getEmojiunicode() {
        return emojiunicode;
    }

    public void setEmojiunicode(String emojiunicode) {
        this.emojiunicode = emojiunicode;
    }

    @SerializedName("emojiunicode")
    @Expose
    private String emojiunicode;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }
}
