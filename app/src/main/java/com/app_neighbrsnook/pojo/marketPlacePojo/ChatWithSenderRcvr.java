package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatWithSenderRcvr {



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("p_id")
    @Expose
    private Integer pId;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("receiver_id")
    @Expose
    private Integer receiverId;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("read_status")
    @Expose
    private Integer readStatus;
    @SerializedName("sender_userpic")
    @Expose
    private String senderUserpic;
    @SerializedName("created_ats")
    @Expose
    private String createdAts;
    @SerializedName("updated_ats")
    @Expose
    private String updatedAts;

    @SerializedName("sendertype")
    @Expose
    private String sendertype;

    public String getSendertype() {
        return sendertype;
    }

    public void setSendertype(String sendertype) {
        this.sendertype = sendertype;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getSenderUserpic() {
        return senderUserpic;
    }

    public void setSenderUserpic(String senderUserpic) {
        this.senderUserpic = senderUserpic;
    }

    public String getCreatedAts() {
        return createdAts;
    }

    public void setCreatedAts(String createdAts) {
        this.createdAts = createdAts;
    }

    public String getUpdatedAts() {
        return updatedAts;
    }

    public void setUpdatedAts(String updatedAts) {
        this.updatedAts = updatedAts;
    }


}
