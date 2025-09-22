package com.app_neighbrsnook.model.faq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Faq {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("faqdata")
    @Expose
    private List<Faqdatum> faqdata;


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

    public List<Faqdatum> getFaqdata() {
        return faqdata;
    }

    public void setFaqdata(List<Faqdatum> faqdata) {
        this.faqdata = faqdata;
    }
}
