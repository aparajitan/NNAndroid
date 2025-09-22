package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GroupListPojo /*implements Serializable*/ {


    @SerializedName("groupid")
    @Expose
    private String groupid;
    @SerializedName("groupname")
    @Expose
    private String groupname;
    @SerializedName("group_type")
    @Expose
    private String groupType;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("membercount")
    @Expose
    private int membercount;
    @SerializedName("getjoin")
    @Expose
    private String getjoin;
    @SerializedName("pendingRequestCount")
    @Expose
    private String pendingRequestCount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("status")
    @Expose
    private String status;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
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

    public int getMembercount() {
        return membercount;
    }

    public void setMembercount(int membercount) {
        this.membercount = membercount;
    }

    public String getGetjoin() {
        return getjoin;
    }

    public void setGetjoin(String getjoin) {
        this.getjoin = getjoin;
    }

    public String getPendingRequestCount() {
        return pendingRequestCount;
    }

    public void setPendingRequestCount(String pendingRequestCount) {
        this.pendingRequestCount = pendingRequestCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
