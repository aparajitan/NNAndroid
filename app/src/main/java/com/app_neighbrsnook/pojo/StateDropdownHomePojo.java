package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StateDropdownHomePojo implements Serializable {


    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    public ArrayList<StateDropdownPojo> nbdata;

/*
    @SerializedName("ndblist")
    @Expose
    public ArrayList<StateDropdownPojo> ndblist;
*/

    @SerializedName("data")
    @Expose
    public ArrayList<StateDropdownPojo> data;

}
