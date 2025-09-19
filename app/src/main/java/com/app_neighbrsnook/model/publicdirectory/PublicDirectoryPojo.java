package com.app_neighbrsnook.model.publicdirectory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PublicDirectoryPojo {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("neighbrhood")
    @Expose
    private String neighbrhood;

    @SerializedName("listdata")
    @Expose
    private List<Listdatum> listdata;

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

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }


    public List<Listdatum> getListdata() {
        return listdata;
    }

    public void setListdata(List<Listdatum> listdata) {
        this.listdata = listdata;
    }
}
