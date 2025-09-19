package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

public class Nbdatum {
    private String id;
    String state_name;
    String city_name;
    String countryname;
    String business_title;
    boolean isSelected;
    String member_title;
    String post_title;
    String aboutus;
    String cat_title;
    String cat_descpt;
    String cat_status;

    String cat_Iamge;
    String cat_create_by;
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    String updated_at;

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getCat_descpt() {
        return cat_descpt;
    }

    public void setCat_descpt(String cat_descpt) {
        this.cat_descpt = cat_descpt;
    }

    public String getCat_status() {
        return cat_status;
    }

    public void setCat_status(String cat_status) {
        this.cat_status = cat_status;
    }

    public String getCat_Iamge() {
        return cat_Iamge;
    }

    public void setCat_Iamge(String cat_Iamge) {
        this.cat_Iamge = cat_Iamge;
    }

    public String getCat_create_by() {
        return cat_create_by;
    }

    public void setCat_create_by(String cat_create_by) {
        this.cat_create_by = cat_create_by;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getMember_title() {
        return member_title;
    }

    public void setMember_title(String member_title) {
        this.member_title = member_title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateName() {
        return state_name;
    }

    public void setStateName(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getBusiness_title() {
        return business_title;
    }

    public void setBusiness_title(String business_title) {
        this.business_title = business_title;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getAboutus() {
        return aboutus;
    }

    public void setAboutus(String aboutus) {
        this.aboutus = aboutus;
    }

}
