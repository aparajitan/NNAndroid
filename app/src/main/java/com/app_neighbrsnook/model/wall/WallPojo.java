package com.app_neighbrsnook.model.wall;

import com.app_neighbrsnook.pojo.marketPlacePojo.AllDataShowPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CategoryMarketPlace;
import com.app_neighbrsnook.pojo.marketPlacePojo.MarketPlaceTodayListPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.WishListPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.YourItemsPojo;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class WallPojo {
    @SerializedName("status")
    private String status;
    @SerializedName("message")

    private String message;
    @SerializedName("member_count")
    private String member_count;
    @SerializedName("favouritstatus")
    private int  favouritstatus;
    @SerializedName("announcement")
    private List<Announcement> announcement;

    @SerializedName("listdata")
    private List<Listdatum> listdata;

    public String getPopup_verified_status() {
        return popup_verified_status;
    }

    public void setPopup_verified_status(String popup_verified_status) {
        this.popup_verified_status = popup_verified_status;
    }

    @SerializedName("popup_verified_status")
    private String popup_verified_status;

    public List<AllDataShowPojo> getAll_products_list() {
        return all_products_list;
    }

    public void setAll_products_list(List<AllDataShowPojo> all_products_list) {
        this.all_products_list = all_products_list;
    }

    @SerializedName("all_products_list")
    private List<AllDataShowPojo> all_products_list;

    public List<YourItemsPojo> getProducttodaylist() {
        return producttodaylist;
    }

    public void setProducttodaylist(List<YourItemsPojo> producttodaylist) {
        this.producttodaylist = producttodaylist;
    }

    @SerializedName("producttodaylist")
    private List<YourItemsPojo> producttodaylist;


    public List<MarketPlaceTodayListPojo> getProductdetail() {
        return productdetail;
    }

    public void setProductdetail(List<MarketPlaceTodayListPojo> productdetail) {
        this.productdetail = productdetail;
    }

    @SerializedName("productdetail")
    private List<MarketPlaceTodayListPojo> productdetail;



    public List<YourItemsPojo> getMyproductlist() {
        return myproductlist;
    }

    public void setMyproductlist(List<YourItemsPojo> myproductlist) {
        this.myproductlist = myproductlist;
    }

    @SerializedName("myproductlist")
    private List<YourItemsPojo> myproductlist;

    public List<MarketPlaceTodayListPojo> getToday_list() {
        return today_list;
    }

    public void setToday_list(List<MarketPlaceTodayListPojo> today_list) {
        this.today_list = today_list;
    }

    public List<YourItemsPojo> getYour_items() {
        return your_items;
    }

    public void setYour_items(List<YourItemsPojo> your_items) {
        this.your_items = your_items;
    }

    @SerializedName("your_items")
    private List<YourItemsPojo> your_items;
    @SerializedName("today_list")
    private List<MarketPlaceTodayListPojo> today_list;

    public List<CategoryMarketPlace> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryMarketPlace> category) {
        this.category = category;
    }

    @SerializedName("category")
    private List<CategoryMarketPlace> category;



    public List<MarketPlaceTodayListPojo> getProducthomelist() {
        return producthomelist;
    }

    public void setProducthomelist(List<MarketPlaceTodayListPojo> producthomelist) {
        this.producthomelist = producthomelist;
    }

    @SerializedName("producthomelist")
    private List<MarketPlaceTodayListPojo> producthomelist;


    public List<CategoryMarketPlace> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryMarketPlace> categories) {
        this.categories = categories;
    }

    @SerializedName("categories")
    private List<CategoryMarketPlace> categories;

    public List<WishListPojo> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<WishListPojo> wishlist) {
        this.wishlist = wishlist;
    }

    @SerializedName("wishlist")
    private List<WishListPojo> wishlist;


    @SerializedName("my_neighborhood_id")
    private String my_neighborhood_id;

    @SerializedName("my_neighborhood")
    private String my_neighborhood;


    @SerializedName("missmatch_remarks")
    private String missmatch_remarks;


    public String getMissmatch_status() {
        return missmatch_status;
    }

    public void setMissmatch_status(String missmatch_status) {
        this.missmatch_status = missmatch_status;
    }

    @SerializedName("missmatch_status")
    private String missmatch_status;

    public String getAwait_status() {
        return await_status;
    }

    public void setAwait_status(String await_status) {
        this.await_status = await_status;
    }

    @SerializedName("await_status")
    private String await_status;

    @SerializedName("verified_status")
    private String verified_status;
    @SerializedName("verfied_msg")
    private String verfied_msg;

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

    public List<Listdatum> getListdata() {
        return listdata;
    }

    public void setListdata(List<Listdatum> listdata) {
        this.listdata = listdata;
    }

    public List<Announcement> getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(List<Announcement> announcement) {
        this.announcement = announcement;
    }

    public String getMember_count() {
        return member_count;
    }

    public void setMember_count(String member_count) {
        this.member_count = member_count;
    }

    public String getVerfied_msg() {
        return verfied_msg;
    }

    public void setVerfied_msg(String verfied_msg) {
        this.verfied_msg = verfied_msg;
    }

    public String getMy_neighborhood() {
        return my_neighborhood;
    }

    public void setMy_neighborhood(String my_neighborhood) {
        this.my_neighborhood = my_neighborhood;
    }

    public String getMissmatch_remarks() {
        return missmatch_remarks;
    }

    public void setMissmatch_remarks(String missmatch_remarks) {
        this.missmatch_remarks = missmatch_remarks;
    }

    public String getVerified_status() {
        return verified_status;
    }

    public void setVerified_status(String verified_status) {
        this.verified_status = verified_status;
    }
    public String getMy_neighborhood_id() {
        return my_neighborhood_id;
    }

    public void setMy_neighborhood_id(String my_neighborhood_id) {
        this.my_neighborhood_id = my_neighborhood_id;
    }
    public int getFavouritstatus() {
        return favouritstatus;
    }

    public void setFavouritstatus(int favouritstatus) {
        this.favouritstatus = favouritstatus;
    }

}
