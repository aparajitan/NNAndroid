package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

public class UploadDocumentResponse {
        @SerializedName("status")
        private String status;
        @SerializedName("message")
        private String message;

        @SerializedName("docsdata")
        public UploadDocumentViewPojo docsdata;
        public UploadDocumentViewPojo getDocsdata() {
                return docsdata;
        }
        public void setDocsdata(UploadDocumentViewPojo docsdata) {
                this.docsdata = docsdata;
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

}
