package com.app_neighbrsnook.model.postComment;

import com.app_neighbrsnook.pojo.eventDetails.EventListComentPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("postlistdata")
    @Expose
    private List<Postlistdatum> postlistdata;

    public List<EventListComentPojo> getEventlistdata() {
        return eventlistdata;
    }

    public void setEventlistdata(List<EventListComentPojo> eventlistdata) {
        this.eventlistdata = eventlistdata;
    }

    @SerializedName("eventlistdata")
    @Expose
    private List<EventListComentPojo> eventlistdata;

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

    public List<Postlistdatum> getPostlistdata() {
        return postlistdata;
    }

    public void setPostlistdata(List<Postlistdatum> postlistdata) {
        this.postlistdata = postlistdata;
    }

}
