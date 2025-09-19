package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventListPojoNbdata implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("eventstartdate")
    @Expose
    private String eventstartdate;
    @SerializedName("eventenddate")
    @Expose
    private String eventenddate;
    @SerializedName("event_starttime")
    @Expose
    private String eventStarttime;
    @SerializedName("event_endtime")
    @Expose
    private String eventEndtime;
    @SerializedName("event_detail")
    @Expose
    private String eventDetail;
    @SerializedName("willeventstart")
    @Expose
    private String willeventstart;
    @SerializedName("iseventrunning")
    @Expose
    private String iseventrunning;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("address")
    @Expose
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    public String getEventStarttime() {
        return eventStarttime;
    }

    public void setEventStarttime(String eventStarttime) {
        this.eventStarttime = eventStarttime;
    }

    public String getEventEndtime() {
        return eventEndtime;
    }

    public void setEventEndtime(String eventEndtime) {
        this.eventEndtime = eventEndtime;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getWilleventstart() {
        return willeventstart;
    }

    public void setWilleventstart(String willeventstart) {
        this.willeventstart = willeventstart;
    }

    public String getIseventrunning() {
        return iseventrunning;
    }

    public void setIseventrunning(String iseventrunning) {
        this.iseventrunning = iseventrunning;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
