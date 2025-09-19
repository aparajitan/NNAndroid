package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JoinEventUserListPojo {



    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("listdata")
    @Expose

        public List<EventDetailsUserListPojo> listdata;
    public List<EventDetailsUserListPojo> getListdata() {
        return listdata;
    }
    public void setListdata(List<EventDetailsUserListPojo> listdata) {
        this.listdata = listdata;
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
