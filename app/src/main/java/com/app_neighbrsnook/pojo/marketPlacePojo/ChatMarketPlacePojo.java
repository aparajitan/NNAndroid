package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatMarketPlacePojo {



    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("sender_userpic")
    @Expose
    private String senderUserpic;
    @SerializedName("neighborhood")
    @Expose
    private String neighborhood;

    @SerializedName("product_id")
    @Expose
    private Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    @SerializedName("read_status")
    @Expose
    private Integer readStatus;
    @SerializedName("read_count")
    @Expose

    private Integer readCount;

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }
    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderUserpic() {
        return senderUserpic;
    }

    public void setSenderUserpic(String senderUserpic) {
        this.senderUserpic = senderUserpic;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

}

