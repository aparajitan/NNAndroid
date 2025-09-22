package com.app_neighbrsnook.pojo.marketPlacePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketPlaceSearchFilterPojo {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("p_title")
    @Expose
    private String pTitle;
    @SerializedName("p_description")
    @Expose
    private String pDescription;
    @SerializedName("p_quantity")
    @Expose
    private Integer pQuantity;
    @SerializedName("sale_type")
    @Expose
    private String saleType;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("cat_id")
    @Expose
    private Integer catId;
    @SerializedName("p_images")
    @Expose
    private String pImages;
    @SerializedName("p_status")
    @Expose
    private Integer pStatus;
    @SerializedName("neighborhood_id")
    @Expose
    private Integer neighborhoodId;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("neighborhood_name")
    @Expose
    private String neighborhoodName;
    @SerializedName("wishlist_status")
    @Expose
    private Integer wishlistStatus;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("updated_time")
    @Expose
    private String updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public Integer getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(Integer pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getpImages() {
        return pImages;
    }

    public void setpImages(String pImages) {
        this.pImages = pImages;
    }

    public Integer getpStatus() {
        return pStatus;
    }

    public void setpStatus(Integer pStatus) {
        this.pStatus = pStatus;
    }

    public Integer getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(Integer neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getNeighborhoodName() {
        return neighborhoodName;
    }

    public void setNeighborhoodName(String neighborhoodName) {
        this.neighborhoodName = neighborhoodName;
    }

    public Integer getWishlistStatus() {
        return wishlistStatus;
    }

    public void setWishlistStatus(Integer wishlistStatus) {
        this.wishlistStatus = wishlistStatus;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

}
