package com.app_neighbrsnook.model;

import com.google.gson.annotations.SerializedName;

public class BusinessModelResponse {
//    {
//        "status": "success",
//            "message": "Published Successfully !!",
//            "businessid": 60
//    }

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("businessid")
    int businessid;

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

    public int getBusinessid() {
        return businessid;
    }

    public void setBusinessid(int businessid) {
        this.businessid = businessid;
    }
}
