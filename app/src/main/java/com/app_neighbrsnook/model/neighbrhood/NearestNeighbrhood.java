package com.app_neighbrsnook.model.neighbrhood;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearestNeighbrhood {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("member")
    @Expose
    private String member;

    @SerializedName("coverimageandroid")
    @Expose
    private String coverimageandroid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getCoverimageandroid() {
        return coverimageandroid;
    }

    public void setCoverimageandroid(String coverimageandroid) {
        this.coverimageandroid = coverimageandroid;
    }
}
