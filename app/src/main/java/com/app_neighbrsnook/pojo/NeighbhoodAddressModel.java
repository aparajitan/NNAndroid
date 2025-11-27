package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NeighbhoodAddressModel {


    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("referrer_neighbourhood_status")
    private int referrerNeighbourhoodStatus;

    @SerializedName("referrer_msg")
    private String referrerMsg;

    @SerializedName("data")
    private List<DataItem> data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getReferrerNeighbourhoodStatus() {
        return referrerNeighbourhoodStatus;
    }

    public String getReferrerMsg() {
        return referrerMsg;
    }

    public List<DataItem> getData() {
        return data;
    }

    public static class DataItem {
        @SerializedName("nbd_id")
        private String nbdId;

        @SerializedName("nbd_name")
        private String nbdName;

        public String getNbdId() {
            return nbdId;
        }

        public String getNbdName() {
            return nbdName;
        }
    }
}
