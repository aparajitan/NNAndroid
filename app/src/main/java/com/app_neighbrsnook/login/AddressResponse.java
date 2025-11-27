package com.app_neighbrsnook.login;

import com.app_neighbrsnook.pojo.DocPojo;
import com.app_neighbrsnook.pojo.UploadDocumentViewPojo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressResponse {
        @SerializedName("status")
        private String status;
        @SerializedName("message")
        private String message;
        @SerializedName("join_status")
        private String joinStatus;
        @SerializedName("image")
        private String image;

        @SerializedName("verfied_msg")


        private String verfied_msg;




        public String getVerfied_msg() {
                return verfied_msg;
        }

        public void setVerfied_msg(String verfied_msg) {
                this.verfied_msg = verfied_msg;
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

        public String getImage() {
                return image;
        }

        public void setImage(String image) {
                this.image = image;
        }

        public String getJoinStatus() {
                return joinStatus;
        }

        public void setJoinStatus(String joinStatus) {
                this.joinStatus = joinStatus;
        }
}
