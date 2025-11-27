package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginPojo implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose

    private String username;
    @SerializedName("emailid")
    @Expose
    private String emailid;
    @SerializedName("phoneno")
    @Expose
    private String phoneno;
    @SerializedName("verified")
    @Expose
    private String verified;
    @SerializedName("membercount")
    @Expose
    private String membercount;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("logintype")
    @Expose
    private String logintype;
    @SerializedName("andlink")
    @Expose
    private String andlink;
    @SerializedName("ioslink")
    @Expose
    private String ioslink;
    @SerializedName("neighbrshood")
    @Expose
    private String neighbrshood;
    @SerializedName("verified_message")
    @Expose
    private String verified_message;

    public String getReq_ndbstatus() {
        return req_ndbstatus;
    }

    public void setReq_ndbstatus(String req_ndbstatus) {
        this.req_ndbstatus = req_ndbstatus;
    }

    @SerializedName("req_ndbstatus")
    private String req_ndbstatus;

    public String getVerified_message() {
        return verified_message;
    }

    public void setVerified_message(String verified_message) {
        this.verified_message = verified_message;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getMembercount() {
        return membercount;
    }

    public void setMembercount(String membercount) {
        this.membercount = membercount;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getAndlink() {
        return andlink;
    }

    public void setAndlink(String andlink) {
        this.andlink = andlink;
    }

    public String getIoslink() {
        return ioslink;
    }

    public void setIoslink(String ioslink) {
        this.ioslink = ioslink;
    }

    public String getNeighbrshood() {
        return neighbrshood;
    }

    public void setNeighbrshood(String neighbrshood) {
        this.neighbrshood = neighbrshood;
    }

}
