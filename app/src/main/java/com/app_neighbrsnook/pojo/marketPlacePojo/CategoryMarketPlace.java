package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryMarketPlace {



        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("cat_title")
        @Expose
        private String catTitle;
        @SerializedName("cat_descpt")
        @Expose
        private String catDescpt;
        @SerializedName("cat_status")
        @Expose
        private Integer catStatus;
        @SerializedName("cat_image")
        @Expose
        private String catIamge;

    @SerializedName("cat_create_by")
    @Expose
    private String catCreateBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCatTitle() {
            return catTitle;
        }

        public void setCatTitle(String catTitle) {
            this.catTitle = catTitle;
        }

        public String getCatDescpt() {
            return catDescpt;
        }

        public void setCatDescpt(String catDescpt) {
            this.catDescpt = catDescpt;
        }

        public Integer getCatStatus() {
            return catStatus;
        }

        public void setCatStatus(Integer catStatus) {
            this.catStatus = catStatus;
        }

        public String getCatIamge() {
            return catIamge;
        }

        public void setCatIamge(String catIamge) {
            this.catIamge = catIamge;
        }

        public String getCatCreateBy() {
            return catCreateBy;
        }

        public void setCatCreateBy(String catCreateBy) {
            this.catCreateBy = catCreateBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }


