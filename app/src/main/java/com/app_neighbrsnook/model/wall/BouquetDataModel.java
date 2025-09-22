package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BouquetDataModel {

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

    @SerializedName("bokay")
    @Expose
    private String bokay;

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

    public String getBokay() {
        return bokay;
    }

    public void setBokay(String bokay) {
        this.bokay = bokay;
    }

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }
}
