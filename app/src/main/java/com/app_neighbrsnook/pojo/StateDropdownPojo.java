package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StateDropdownPojo {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("nbd_id")
    @Expose
    public String nbdId;
    @SerializedName("state_name")
    @Expose
    public String stateName;
    @SerializedName("city_name")
    @Expose
    public String cityteName;
    @SerializedName("nbd_name")
    @Expose
    public String nbd_name;
    @SerializedName("ndbname")
    @Expose
    public String ndbname;

/*

    @SerializedName("ndblist")
    @Expose
    public String ndblist;
*/

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getNbd_name() {
        return nbd_name;
    }

    public void setNbd_name(String nbd_name) {
        this.nbd_name = nbd_name;
    }
/*
    public String getNdblist() {
        return ndblist;
    }

    public void setNdblist(String ndblist) {
        this.ndblist = ndblist;
    }*/
}
