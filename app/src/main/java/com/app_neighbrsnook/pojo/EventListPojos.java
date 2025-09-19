package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventListPojos {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("evencount_past")
    @Expose
    private int evencountPast;
    @SerializedName("eventcount_current")
    @Expose
    private int eventcountCurrent;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;

    public String getVerfied_msg() {
        return verfied_msg;
    }

    public void setVerfied_msg(String verfied_msg) {
        this.verfied_msg = verfied_msg;
    }

    @SerializedName("verfied_msg")
    @Expose
    private String verfied_msg;

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }
    @SerializedName("eventcount_future")
    @Expose
    private int eventcountFuture;

    public int getEvencountPast() {
        return evencountPast;
    }

    public void setEvencountPast(int evencountPast) {
        this.evencountPast = evencountPast;
    }

    public int getEventcountCurrent() {
        return eventcountCurrent;
    }

    public void setEventcountCurrent(int eventcountCurrent) {
        this.eventcountCurrent = eventcountCurrent;
    }

    public int getEventcountFuture() {
        return eventcountFuture;
    }

    public void setEventcountFuture(int eventcountFuture) {
        this.eventcountFuture = eventcountFuture;
    }
    @SerializedName("event_past")
    @Expose
    private List<EventListPojoNbdata> eventPast;
    @SerializedName("event_current")
    @Expose
    private List<EventListPojoNbdata> eventCurrent;
    @SerializedName("event_future")
    @Expose
    private List<EventListPojoNbdata> eventFuture;

    public List<EventListPojoNbdata> getEventPast() {
        return eventPast;
    }

    public void setEventPast(List<EventListPojoNbdata> eventPast) {
        this.eventPast = eventPast;
    }

    public List<EventListPojoNbdata> getEventCurrent() {
        return eventCurrent;
    }

    public void setEventCurrent(List<EventListPojoNbdata> eventCurrent) {
        this.eventCurrent = eventCurrent;
    }

    public List<EventListPojoNbdata> getEventFuture() {
        return eventFuture;
    }

    public void setEventFuture(List<EventListPojoNbdata> eventFuture) {
        this.eventFuture = eventFuture;
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





