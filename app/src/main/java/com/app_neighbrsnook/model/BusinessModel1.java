package com.app_neighbrsnook.model;

import com.google.gson.annotations.SerializedName;

public class BusinessModel1 {

    @SerializedName("businessname")
    String businessname;

    @SerializedName("cat")
    Integer cat;

    @SerializedName("description")
    String description;

    @SerializedName("tagline")
    String tagline;

    @SerializedName("image")
    String image;

    @SerializedName("opentime")
    String opentime;

    @SerializedName("closetime")
    String closetime;

    @SerializedName("weekoff")
    String weekoff;

    @SerializedName("address1")
    String address1;

    @SerializedName("address2")
    String address2;

    @SerializedName("country")
    Integer country;

    @SerializedName("city")
    String city;

    @SerializedName("state")
    Integer state;

    @SerializedName("pin")
    String pin;

    @SerializedName("web")
    String web;

    @SerializedName("email")
    String email;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("telephone")
    String telephone;

    @SerializedName("document")
    String document;

    @SerializedName("userid")
    Integer userid;

    @SerializedName("neighbrhood")
    String neighbrhood;

//    @SerializedName("editid")
//    String editid;


    public BusinessModel1(String businessname, Integer cat, String description, String tagline, String image, String opentime, String closetime, String weekoff,
                          String address1, String address2, Integer country, String city, Integer state, String pin, String web, String email, String mobile, String telephone, String document, Integer userid, String neighbrhood) {

        this.businessname=businessname;
        this.cat = cat;
        this.description = description;
        this.tagline = tagline;
        this.image = image;
        this.opentime = opentime;
        this.closetime = closetime;
        this.weekoff = weekoff;
        this.address1 = address1;
        this.address2 = address2;
        this.country = country;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.web = web;
        this.email = email;
        this.mobile = mobile;
        this.telephone = telephone;
        this.document = document;
        this.userid = userid;
        this.neighbrhood = neighbrhood;
    }


    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname= businessname;
    }

    public Integer getCat() {
        return cat;
    }

    public void setCat(Integer cat) {
        this.cat = cat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    public String getWeekoff() {
        return weekoff;
    }

    public void setWeekoff(String weekoff) {
        this.weekoff = weekoff;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }
}
