package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusinessListPojo {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;


    @SerializedName("addlinetwo")
    public String addlinetwo;

    @SerializedName("state_name")
    public String state_name;

    @SerializedName("city_name")
    public String city_name;

    @SerializedName("pin")
    public String pin;


    @SerializedName("verfied_msg")

    public String verfied_msg;
    @SerializedName("listdata")
    public List<Listdatum> listdata;

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

    public String getVerfied_msg() {
        return verfied_msg;
    }

    public void setVerfied_msg(String verfied_msg) {
        this.verfied_msg = verfied_msg;
    }

    public List<Listdatum> getListdata() {
        return listdata;
    }

    public void setListdata(List<Listdatum> listdata) {
        this.listdata = listdata;
    }

    public String getAddlinetwo() {
        return addlinetwo;
    }

    public void setAddlinetwo(String addlinetwo) {
        this.addlinetwo = addlinetwo;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
