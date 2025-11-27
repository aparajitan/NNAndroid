package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketPlaceWallPojo {



    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("neighborhood")
    @Expose
    private String neighborhood;
    @SerializedName("verfied_msg")
    @Expose
    private String verfiedMsg;

    public List<MarketPlaceSearchFilterPojo> getProducthomelist() {
        return producthomelist;
    }

    public void setProducthomelist(List<MarketPlaceSearchFilterPojo> producthomelist) {
        this.producthomelist = producthomelist;
    }

    @SerializedName("producthomelist")
    private List<MarketPlaceSearchFilterPojo> producthomelist;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getVerfiedMsg() {
        return verfiedMsg;
    }

    public void setVerfiedMsg(String verfiedMsg) {
        this.verfiedMsg = verfiedMsg;
    }

}
