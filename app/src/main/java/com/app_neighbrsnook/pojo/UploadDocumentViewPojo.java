package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public class UploadDocumentViewPojo {
    @SerializedName("id")

    private String id;
    @SerializedName("passport_front")

    private String passportFront;
    @SerializedName("passport_back")

    private String passportBack;
    @SerializedName("aadhar_front")

    public String aadharFront;
    @SerializedName("aadhar_back")

    private String aadharBack;
    @SerializedName("voterid_front")

    private String voteridFront;
    @SerializedName("voterid_back")

    private String voteridBack;
    @SerializedName("driving_license_front")

    private String drivingLicenseFront;
    @SerializedName("driving_license_back")

    private String drivingLicenseBack;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassportFront() {
        return passportFront;
    }

    public void setPassportFront(String passportFront) {
        this.passportFront = passportFront;
    }

    public String getPassportBack() {
        return passportBack;
    }

    public void setPassportBack(String passportBack) {
        this.passportBack = passportBack;
    }

    public String getAadharFront() {
        return aadharFront;
    }

    public void setAadharFront(String aadharFront) {
        this.aadharFront = aadharFront;
    }

    public String getAadharBack() {
        return aadharBack;
    }

    public void setAadharBack(String aadharBack) {
        this.aadharBack = aadharBack;
    }

    public String getVoteridFront() {
        return voteridFront;
    }

    public void setVoteridFront(String voteridFront) {
        this.voteridFront = voteridFront;
    }

    public String getVoteridBack() {
        return voteridBack;
    }

    public void setVoteridBack(String voteridBack) {
        this.voteridBack = voteridBack;
    }

    public String getDrivingLicenseFront() {
        return drivingLicenseFront;
    }

    public void setDrivingLicenseFront(String drivingLicenseFront) {
        this.drivingLicenseFront = drivingLicenseFront;
    }

    public String getDrivingLicenseBack() {
        return drivingLicenseBack;
    }

    public void setDrivingLicenseBack(String drivingLicenseBack) {
        this.drivingLicenseBack = drivingLicenseBack;
    }

}
