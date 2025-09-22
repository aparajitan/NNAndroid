package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

public class DocTest {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("documentid")
    private Integer documentid;
    @SerializedName("documenturl")
    private String documenturl;

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

    public Integer getDocumentid() {
        return documentid;
    }

    public void setDocumentid(Integer documentid) {
        this.documentid = documentid;
    }

    public String getDocumenturl() {
        return documenturl;
    }

    public void setDocumenturl(String documenturl) {
        this.documenturl = documenturl;
    }

}
