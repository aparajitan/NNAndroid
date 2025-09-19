package com.app_neighbrsnook.model.notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

    String title;
    String desc;
    String category;
    int color;
    int drawable;


    @SerializedName("nbdata")
   
    private List<Nbdatum> nbdata;
    @SerializedName("status")

    private String status;
    @SerializedName("message")
   
    private String message;

    public NotificationModel(String title, String desc, String category, int color,  int drawable) {
        this.title = title;
        this.desc = desc;
        this.category = category;
        this.color = color;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }


    public List<Nbdatum> getNbdata() {
        return nbdata;
    }

    public void setNbdata(List<Nbdatum> nbdata) {
        this.nbdata = nbdata;
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
