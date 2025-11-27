package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeDataModel {

    @SerializedName("welcomeid")
    @Expose
    private String welcomeid;

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

    @SerializedName("like")
    @Expose
    private String like;

    @SerializedName("createon")
    @Expose
    private String createon;

    public String getWelcomeid() {
        return welcomeid;
    }

    public void setWelcomeid(String welcomeid) {
        this.welcomeid = welcomeid;
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

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }
}
