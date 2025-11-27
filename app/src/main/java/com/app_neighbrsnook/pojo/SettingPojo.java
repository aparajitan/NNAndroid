package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingPojo {
    @SerializedName("SendMessageMarketMB")
    @Expose
    private String marketPhone;

    @SerializedName("SendMessageMarketEmail")
    @Expose
    private String marketMail;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("commentOnYourPostsMb")
    @Expose
    private String commentOnYourPostsMb;
    @SerializedName("commentpostmail")
    @Expose
    private String commentpostmail;
    @SerializedName("commentLikesOnYourSuggestionMb")
    @Expose
    private String commentLikesOnYourSuggestionMb;
    @SerializedName("commentsuggestionmail")
    @Expose
    private String commentsuggestionmail;
    @SerializedName("pollForVoteMb")
    @Expose
    private String pollForVoteMb;
    @SerializedName("pollvotemail")
    @Expose
    private String pollvotemail;
    @SerializedName("notificationForEventMb")
    @Expose
    private String notificationForEventMb;
    @SerializedName("notificationeventmail")
    @Expose
    private String notificationeventmail;
    @SerializedName("directMessageMb")
    @Expose
    private String directMessageMb;
    @SerializedName("directmsgmail")
    @Expose
    private String directmsgmail;
    @SerializedName("newGroupMb")
    @Expose
    private String newGroupMb;
    @SerializedName("groupcreatemail")

    @Expose

    private String groupcreatemail;
    @SerializedName("rating_reviewsOnYourBusinessMb")
    @Expose
    private String ratingReviewsOnYourBusinessMb;
    @SerializedName("rating_commentbusinessmail")
    @Expose
    private String ratingCommentbusinessmail;
    @SerializedName("emergencyContactno")
    @Expose
    private String emergencyContactno;
    @SerializedName("addresslineone")
    @Expose
    private String addresslineone;
    @SerializedName("addresslinetwo")
    @Expose
    private String addresslinetwo;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("contactNo")
    @Expose
    private String contactNo;

    @SerializedName("address")
    @Expose
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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

    public String getCommentOnYourPostsMb() {
        return commentOnYourPostsMb;
    }

    public void setCommentOnYourPostsMb(String commentOnYourPostsMb) {
        this.commentOnYourPostsMb = commentOnYourPostsMb;
    }

    public String getCommentpostmail() {
        return commentpostmail;
    }

    public void setCommentpostmail(String commentpostmail) {
        this.commentpostmail = commentpostmail;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCommentLikesOnYourSuggestionMb() {
        return commentLikesOnYourSuggestionMb;
    }

    public void setCommentLikesOnYourSuggestionMb(String commentLikesOnYourSuggestionMb) {
        this.commentLikesOnYourSuggestionMb = commentLikesOnYourSuggestionMb;
    }

    public String getCommentsuggestionmail() {
        return commentsuggestionmail;
    }

    public void setCommentsuggestionmail(String commentsuggestionmail) {
        this.commentsuggestionmail = commentsuggestionmail;
    }

    public String getPollForVoteMb() {
        return pollForVoteMb;
    }

    public void setPollForVoteMb(String pollForVoteMb) {
        this.pollForVoteMb = pollForVoteMb;
    }

    public String getPollvotemail() {
        return pollvotemail;
    }

    public void setPollvotemail(String pollvotemail) {
        this.pollvotemail = pollvotemail;
    }

    public String getNotificationForEventMb() {
        return notificationForEventMb;
    }

    public void setNotificationForEventMb(String notificationForEventMb) {
        this.notificationForEventMb = notificationForEventMb;
    }

    public String getNotificationeventmail() {
        return notificationeventmail;
    }

    public void setNotificationeventmail(String notificationeventmail) {
        this.notificationeventmail = notificationeventmail;
    }

    public String getDirectMessageMb() {
        return directMessageMb;
    }

    public void setDirectMessageMb(String directMessageMb) {
        this.directMessageMb = directMessageMb;
    }

    public String getDirectmsgmail() {
        return directmsgmail;
    }

    public void setDirectmsgmail(String directmsgmail) {
        this.directmsgmail = directmsgmail;
    }

    public String getNewGroupMb() {
        return newGroupMb;
    }

    public void setNewGroupMb(String newGroupMb) {
        this.newGroupMb = newGroupMb;
    }

    public String getGroupcreatemail() {
        return groupcreatemail;
    }

    public void setGroupcreatemail(String groupcreatemail) {
        this.groupcreatemail = groupcreatemail;
    }

    public String getRatingReviewsOnYourBusinessMb() {
        return ratingReviewsOnYourBusinessMb;
    }

    public void setRatingReviewsOnYourBusinessMb(String ratingReviewsOnYourBusinessMb) {
        this.ratingReviewsOnYourBusinessMb = ratingReviewsOnYourBusinessMb;
    }

    public String getRatingCommentbusinessmail() {
        return ratingCommentbusinessmail;
    }

    public void setRatingCommentbusinessmail(String ratingCommentbusinessmail) {
        this.ratingCommentbusinessmail = ratingCommentbusinessmail;
    }

    public String getEmergencyContactno() {
        return emergencyContactno;
    }

    public void setEmergencyContactno(String emergencyContactno) {
        this.emergencyContactno = emergencyContactno;
    }

    public String getAddresslineone() {
        return addresslineone;
    }

    public void setAddresslineone(String addresslineone) {
        this.addresslineone = addresslineone;
    }

    public String getAddresslinetwo() {
        return addresslinetwo;
    }

    public void setAddresslinetwo(String addresslinetwo) {
        this.addresslinetwo = addresslinetwo;
    }

    public String getMarketPhone() {
        return marketPhone;
    }

    public void setMarketPhone(String marketPhone) {
        this.marketPhone = marketPhone;
    }

    public String getMarketMail() {
        return marketMail;
    }

    public void setMarketMail(String marketMail) {
        this.marketMail = marketMail;
    }
}




