package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersListChatsPojo {



        @SerializedName("chats")
        @Expose
        private List<ChatMarketPlacePojo> chats;

        public List<ChatMarketPlacePojo> getChats() {
            return chats;
        }

        public void setChats(List<ChatMarketPlacePojo> chats) {
            this.chats = chats;
        }



}
