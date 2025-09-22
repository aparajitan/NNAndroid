package com.app_neighbrsnook.model.neighbrhood;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("verfied_msg")
    @Expose
    private String verfied_msg;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;
    @SerializedName("totmember")
    @Expose
    private Integer totmember;
    @SerializedName("members")
    @Expose
    private String members;
    @SerializedName("groups")
    @Expose
    private String groups;
    @SerializedName("events")
    @Expose
    private String events;
    @SerializedName("polls")
    @Expose
    private String polls;
    @SerializedName("business")
    @Expose
    private String business;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("suggestions")
    @Expose
    private String suggestions;
    @SerializedName("nearest_neighbrhood")
    @Expose
    private List<NearestNeighbrhood> nearestNeighbrhood;

    @SerializedName("owner_neighbrhood")
    @Expose
    private List<OwnerNeighbrhood> ownerNeighbrhood;

    @SerializedName("groupuserlist")
    @Expose
    private String groupuserlist;
    @SerializedName("eventuserlist")
    @Expose
    private String eventuserlist;
    @SerializedName("polluserlist")
    @Expose
    private String polluserlist;
    @SerializedName("businessuserlist")
    @Expose
    private String businessuserlist;
    @SerializedName("suggestionuserlist")
    @Expose
    private String suggestionuserlist;
    @SerializedName("coverimageandroid")
    @Expose
    private String coverimageandroid;
    @SerializedName("coverimageios")
    @Expose
    private String coverimageios;

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

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

    public Integer getTotmember() {
        return totmember;
    }

    public void setTotmember(Integer totmember) {
        this.totmember = totmember;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getPolls() {
        return polls;
    }

    public void setPolls(String polls) {
        this.polls = polls;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public List<NearestNeighbrhood> getNearestNeighbrhood() {
        return nearestNeighbrhood;
    }

    public void setNearestNeighbrhood(List<NearestNeighbrhood> nearestNeighbrhood) {
        this.nearestNeighbrhood = nearestNeighbrhood;
    }

    public String getGroupuserlist() {
        return groupuserlist;
    }

    public void setGroupuserlist(String groupuserlist) {
        this.groupuserlist = groupuserlist;
    }

    public String getEventuserlist() {
        return eventuserlist;
    }

    public void setEventuserlist(String eventuserlist) {
        this.eventuserlist = eventuserlist;
    }

    public String getPolluserlist() {
        return polluserlist;
    }

    public void setPolluserlist(String polluserlist) {
        this.polluserlist = polluserlist;
    }

    public String getBusinessuserlist() {
        return businessuserlist;
    }

    public void setBusinessuserlist(String businessuserlist) {
        this.businessuserlist = businessuserlist;
    }

    public String getSuggestionuserlist() {
        return suggestionuserlist;
    }

    public void setSuggestionuserlist(String suggestionuserlist) {
        this.suggestionuserlist = suggestionuserlist;
    }

    public String getCoverimageandroid() {
        return coverimageandroid;
    }

    public void setCoverimageandroid(String coverimageandroid) {
        this.coverimageandroid = coverimageandroid;
    }

    public String getCoverimageios() {
        return coverimageios;
    }

    public void setCoverimageios(String coverimageios) {
        this.coverimageios = coverimageios;
    }

    public List<OwnerNeighbrhood> getOwnerNeighbrhood() {
        return ownerNeighbrhood;
    }

    public void setOwnerNeighbrhood(List<OwnerNeighbrhood> ownerNeighbrhood) {
        this.ownerNeighbrhood = ownerNeighbrhood;
    }
}