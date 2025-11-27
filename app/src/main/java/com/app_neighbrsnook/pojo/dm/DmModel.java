package com.app_neighbrsnook.pojo.dm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DmModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("messages")
    @Expose
    private String messages;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("nbdata")
    @Expose
    private List<Nbdatum> nbdata;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<Nbdatum> getNbdata() {
        return nbdata;
    }

    public void setNbdata(List<Nbdatum> nbdata) {
        this.nbdata = nbdata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
