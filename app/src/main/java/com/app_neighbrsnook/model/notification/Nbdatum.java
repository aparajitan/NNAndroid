package com.app_neighbrsnook.model.notification;

import com.google.gson.annotations.SerializedName;

public class Nbdatum {

    @SerializedName("notification_id")
   
    private String notificationId;

    @SerializedName("user_name")

    private String user_name;

    @SerializedName("user_img")

    private String user_img;
    @SerializedName("notification_type")
   
    private String notificationType;
    @SerializedName("id")
   
    private String id;
    @SerializedName("neighbrhood")
   
    private String neighbrhood;
    @SerializedName("title")
   
    private String title;
    @SerializedName("message")
   
    private String message;
    @SerializedName("createon")
   
    private String createon;
    @SerializedName("status")
   
    private String isread;

    @SerializedName("group_type")
    private String grouptype;

    @SerializedName("group_status")
    private String group_status;

    @SerializedName("getjoin")
    private String getjoin;

    @SerializedName("is_delete")
    private String is_delet;

    @SerializedName("userid")
    private String userid;

    @SerializedName("sender_id")
    private String sender_id;

    @SerializedName("sender_idg")
    private String sender_idg;

    public String getGroupchat_name() {
        return groupchat_name;
    }

    public void setGroupchat_name(String groupchat_name) {
        this.groupchat_name = groupchat_name;
    }

    @SerializedName("groupchat_name")
    private String groupchat_name;

    public String getGroupchat_image() {
        return groupchat_image;
    }

    public void setGroupchat_image(String groupchat_image) {
        this.groupchat_image = groupchat_image;
    }

    @SerializedName("groupchat_image")
    private String groupchat_image;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCreateOwner() {
        return createOwner;
    }

    public void setCreateOwner(String createOwner) {
        this.createOwner = createOwner;
    }

    public String getOwnerPic() {
        return ownerPic;
    }

    public void setOwnerPic(String ownerPic) {
        this.ownerPic = ownerPic;
    }

    @SerializedName("created-Owner")
    private String createOwner;

    @SerializedName("ownername")
    private String ownerName;

    @SerializedName("ownerpic")
    private String ownerPic;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

    public String getGroup_status() {
        return group_status;
    }

    public void setGroup_status(String group_status) {
        this.group_status = group_status;
    }

    public String getGetjoin() {
        return getjoin;
    }

    public void setGetjoin(String getjoin) {
        this.getjoin = getjoin;
    }

    public String getIs_delet() {
        return is_delet;
    }

    public void setIs_delet(String is_delet) {
        this.is_delet = is_delet;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_idg() {
        return sender_idg;
    }

    public void setSender_idg(String sender_idg) {
        this.sender_idg = sender_idg;
    }
}
