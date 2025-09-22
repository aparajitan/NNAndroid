package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class  GroupDetailsPojo {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("groupid")
    @Expose
    private String groupid;
    @SerializedName("groupname")
    @Expose
    private String groupname;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;
    @SerializedName("nearbyneighbrhood")
    @Expose
    private String nearbyneighbrhood;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("createdby")
    @Expose
    private String createdby;
    @SerializedName("group_type")
    @Expose
    private String groupType;
    @SerializedName("membercount")
    @Expose
    private int membercount;


    @SerializedName("memb_join_status")
    @Expose
    private Integer membJoinStatus;

    public Integer getMembJoinStatus() {
        return membJoinStatus;
    }

    public void setMembJoinStatus(Integer membJoinStatus) {
        this.membJoinStatus = membJoinStatus;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

    public String getNearbyneighbrhood() {
        return nearbyneighbrhood;
    }

    public void setNearbyneighbrhood(String nearbyneighbrhood) {
        this.nearbyneighbrhood = nearbyneighbrhood;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getMembercount() {
        return membercount;
    }

    public void setMembercount(int membercount) {
        this.membercount = membercount;
    }


}
