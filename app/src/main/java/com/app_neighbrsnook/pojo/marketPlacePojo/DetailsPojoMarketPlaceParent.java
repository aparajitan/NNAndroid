package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.app_neighbrsnook.model.SellParenetModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailsPojoMarketPlaceParent {


        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("productdetail")
        @Expose
        private List<DetailsPojoChild> productdetail;

    public List<MarketPlaceTodayListPojo> getSimilarproducts() {
        return similarproducts;
    }

    public void setSimilarproducts(List<MarketPlaceTodayListPojo> similarproducts) {
        this.similarproducts = similarproducts;
    }

    private List<MarketPlaceTodayListPojo> similarproducts;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<DetailsPojoChild> getProductdetail() {
            return productdetail;
        }

        public void setProductdetail(List<DetailsPojoChild> productdetail) {
            this.productdetail = productdetail;
        }

    }