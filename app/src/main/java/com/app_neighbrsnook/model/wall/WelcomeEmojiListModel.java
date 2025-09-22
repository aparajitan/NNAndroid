package com.app_neighbrsnook.model.wall;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WelcomeEmojiListModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")

    private String message;

    @SerializedName("likedata")
    private List<LikeDataModel> likeDataModel;

    @SerializedName("bokaydata")
    private List<BouquetDataModel> bouquetDataModel;

    @SerializedName("total_like")
    private String totalLike;

    @SerializedName("total_bokay")
    private String totalBokay;

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

    public List<LikeDataModel> getLikeDataModel() {
        return likeDataModel;
    }

    public void setLikeDataModel(List<LikeDataModel> likeDataModel) {
        this.likeDataModel = likeDataModel;
    }

    public List<BouquetDataModel> getBouquetDataModel() {
        return bouquetDataModel;
    }

    public void setBouquetDataModel(List<BouquetDataModel> bouquetDataModel) {
        this.bouquetDataModel = bouquetDataModel;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getTotalBokay() {
        return totalBokay;
    }

    public void setTotalBokay(String totalBokay) {
        this.totalBokay = totalBokay;
    }
}
