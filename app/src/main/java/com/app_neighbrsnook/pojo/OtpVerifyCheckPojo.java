package com.app_neighbrsnook.pojo;

import android.util.EventLogTags;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpVerifyCheckPojo {

        @SerializedName("desc")
        @Expose
        private String desc;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("description")
        @Expose
        private Object  description;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object  getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

    }

