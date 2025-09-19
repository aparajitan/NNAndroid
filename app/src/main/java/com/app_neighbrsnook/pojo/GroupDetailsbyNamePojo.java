package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class  GroupDetailsbyNamePojo implements Serializable {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("groupid")
    @Expose
    private String groupid;
/*
    @SerializedName("memberlist")
    @Expose
    private List<GroupDetailsbyNameResponse> memberlist;
*/
/*

    public List<GroupDetailsbyNameResponse> getMemberlist() {
        return memberlist;
    }

    public void setMemberlist(List<GroupDetailsbyNameResponse> memberlist) {
        this.memberlist = memberlist;
    }
*/

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

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
