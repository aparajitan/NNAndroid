package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentChatWithSenderRcvr {


    @SerializedName("messages")
    @Expose
    private List<ChatWithSenderRcvr> messages;

    public List<ChatWithSenderRcvr> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatWithSenderRcvr> messages) {
        this.messages = messages;
    }
}
