package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatePojo {
  /*  @SerializedName("status")
    @Expose
    private String status;
    private String message;
    @SerializedName("notification_count")
    @Expose
    private int notification_count;
    private String post_img_limit;
    private String post_image_size;
    private String post_video_size;
    private String post_video_limit;

    public String getPost_video_limit() {
        return post_video_limit;
    }

    public void setPost_video_limit(String post_video_limit) {
        this.post_video_limit = post_video_limit;
    }

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
    @SerializedName("business_video_size")
    @Expose
    private String businessVideoSize;
    private List<Nbdatum> nbdata;
    private List<Nbdatum> category;

    public List<Nbdatum> getCategory() {
        return category;
    }

    public void setCategory(List<Nbdatum> category) {
        this.category = category;
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

    public List<Nbdatum> getNbdata() {
        return nbdata;
    }

    public void setNbdata(List<Nbdatum> nbdata) {
        this.nbdata = nbdata;
    }

    public int getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(int notification_count) {
        this.notification_count = notification_count;
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

    public String getBusinessVideoSize() {
        return businessVideoSize;
    }

    public void setBusinessVideoSize(String businessVideoSize) {
        this.businessVideoSize = businessVideoSize;
    }

    public String getPost_img_limit() {
        return post_img_limit;
    }

    public void setPost_img_limit(String post_img_limit) {
        this.post_img_limit = post_img_limit;
    }

    public String getPost_image_size() {
        return post_image_size;
    }

    public void setPost_image_size(String post_image_size) {
        this.post_image_size = post_image_size;
    }

    public String getPost_video_size() {
        return post_video_size;
    }

    public void setPost_video_size(String post_video_size) {
        this.post_video_size = post_video_size;
    }*/






        @SerializedName("status")
        @Expose
        private String status;
        private String message;
        private String post_img_limit;
        private String post_image_size;
        private String post_video_size;
        private String post_video_limit;

        @SerializedName("notification_count")
        @Expose
        private int notification_count;

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
        private String mkpImageLimit;

        @SerializedName("mpk_product_img_size")
        @Expose
        private String mkpImageSize;

        @SerializedName("mpk_product_video_limit")
        @Expose
        private String mkpVideoLimit;

        @SerializedName("mpk_product_video_size")
        @Expose
        private String mkpVideoSize;

        private List<Nbdatum> nbdata;

        private List<Nbdatum> category;

        public List<Nbdatum> getCategory() {
            return category;
        }

        public void setCategory(List<Nbdatum> category) {
            this.category = category;
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

        public List<Nbdatum> getNbdata() {
            return nbdata;
        }

        public void setNbdata(List<Nbdatum> nbdata) {
            this.nbdata = nbdata;
        }

        public int getNotification_count() {
            return notification_count;
        }

        public void setNotification_count(int notification_count) {
            this.notification_count = notification_count;
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

        public String getPost_img_limit() {
            return post_img_limit;
        }

        public void setPost_img_limit(String post_img_limit) {
            this.post_img_limit = post_img_limit;
        }

        public String getPost_image_size() {
            return post_image_size;
        }

        public void setPost_image_size(String post_image_size) {
            this.post_image_size = post_image_size;
        }

        public String getPost_video_size() {
            return post_video_size;
        }

        public void setPost_video_size(String post_video_size) {
            this.post_video_size = post_video_size;
        }

        public String getPost_video_limit() {
            return post_video_limit;
        }

        public void setPost_video_limit(String post_video_limit) {
            this.post_video_limit = post_video_limit;
        }

        public String getMkpImageLimit() {
            return mkpImageLimit;
        }

        public void setMkpImageLimit(String mkpImageLimit) {
            this.mkpImageLimit = mkpImageLimit;
        }

        public String getMkpImageSize() {
            return mkpImageSize;
        }

        public void setMkpImageSize(String mkpImageSize) {
            this.mkpImageSize = mkpImageSize;
        }

        public String getMkpVideoLimit() {
            return mkpVideoLimit;
        }

        public void setMkpVideoLimit(String mkpVideoLimit) {
            this.mkpVideoLimit = mkpVideoLimit;
        }

        public String getMkpVideoSize() {
            return mkpVideoSize;
        }

        public void setMkpVideoSize(String mkpVideoSize) {
            this.mkpVideoSize = mkpVideoSize;


        }



}


