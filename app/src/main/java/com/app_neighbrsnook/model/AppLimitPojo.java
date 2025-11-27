package com.app_neighbrsnook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppLimitPojo {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("post_img_limit")
    @Expose
    private String postImgLimit;
    @SerializedName("post_image_size")
    @Expose
    private String postImageSize;
    @SerializedName("post_video_limit")
    @Expose
    private String postVideoLimit;
    @SerializedName("post_video_size")
    @Expose
    private String postVideoSize;
    @SerializedName("event_img_limit")
    @Expose
    private String eventImgLimit;
    @SerializedName("event_image_size")
    @Expose
    private String eventImageSize;
    @SerializedName("business_img_limit")
    @Expose
    private String businessImgLimit;
    @SerializedName("business_image_size")
    @Expose
    private String businessImageSize;
    @SerializedName("business_doc_limit")
    @Expose
    private String businessDocLimit;
    @SerializedName("business_doc_size")
    @Expose
    private String businessDocSize;
    @SerializedName("business_video_limit")
    @Expose
    private String businessVideoLimit;
    @SerializedName("business_video_size")
    @Expose
    private String businessVideoSize;
    @SerializedName("mpk_product_img_limit")
    @Expose
    private String mpkProductImgLimit;
    @SerializedName("mpk_product_img_size")
    @Expose
    private String mpkProductImgSize;
    @SerializedName("mpk_product_video_limit")
    @Expose
    private String mpkProductVideoLimit;
    @SerializedName("mpk_product_video_size")
    @Expose
    private String mpkProductVideoSize;

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

    public String getPostImgLimit() {
        return postImgLimit;
    }

    public void setPostImgLimit(String postImgLimit) {
        this.postImgLimit = postImgLimit;
    }

    public String getPostImageSize() {
        return postImageSize;
    }

    public void setPostImageSize(String postImageSize) {
        this.postImageSize = postImageSize;
    }

    public String getPostVideoLimit() {
        return postVideoLimit;
    }

    public void setPostVideoLimit(String postVideoLimit) {
        this.postVideoLimit = postVideoLimit;
    }

    public String getPostVideoSize() {
        return postVideoSize;
    }

    public void setPostVideoSize(String postVideoSize) {
        this.postVideoSize = postVideoSize;
    }

    public String getEventImgLimit() {
        return eventImgLimit;
    }

    public void setEventImgLimit(String eventImgLimit) {
        this.eventImgLimit = eventImgLimit;
    }

    public String getEventImageSize() {
        return eventImageSize;
    }

    public void setEventImageSize(String eventImageSize) {
        this.eventImageSize = eventImageSize;
    }

    public String getBusinessImgLimit() {
        return businessImgLimit;
    }

    public void setBusinessImgLimit(String businessImgLimit) {
        this.businessImgLimit = businessImgLimit;
    }

    public String getBusinessImageSize() {
        return businessImageSize;
    }

    public void setBusinessImageSize(String businessImageSize) {
        this.businessImageSize = businessImageSize;
    }

    public String getBusinessDocLimit() {
        return businessDocLimit;
    }

    public void setBusinessDocLimit(String businessDocLimit) {
        this.businessDocLimit = businessDocLimit;
    }

    public String getBusinessDocSize() {
        return businessDocSize;
    }

    public void setBusinessDocSize(String businessDocSize) {
        this.businessDocSize = businessDocSize;
    }

    public String getBusinessVideoLimit() {
        return businessVideoLimit;
    }

    public void setBusinessVideoLimit(String businessVideoLimit) {
        this.businessVideoLimit = businessVideoLimit;
    }

    public String getBusinessVideoSize() {
        return businessVideoSize;
    }

    public void setBusinessVideoSize(String businessVideoSize) {
        this.businessVideoSize = businessVideoSize;
    }

    public String getMpkProductImgLimit() {
        return mpkProductImgLimit;
    }

    public void setMpkProductImgLimit(String mpkProductImgLimit) {
        this.mpkProductImgLimit = mpkProductImgLimit;
    }

    public String getMpkProductImgSize() {
        return mpkProductImgSize;
    }

    public void setMpkProductImgSize(String mpkProductImgSize) {
        this.mpkProductImgSize = mpkProductImgSize;
    }

    public String getMpkProductVideoLimit() {
        return mpkProductVideoLimit;
    }

    public void setMpkProductVideoLimit(String mpkProductVideoLimit) {
        this.mpkProductVideoLimit = mpkProductVideoLimit;
    }

    public String getMpkProductVideoSize() {
        return mpkProductVideoSize;
    }

    public void setMpkProductVideoSize(String mpkProductVideoSize) {
        this.mpkProductVideoSize = mpkProductVideoSize;
    }
}
