package com.app_neighbrsnook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "business")
public class BusinessModel {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "businessname")
    String businessname;

    @ColumnInfo(name = "cat")
    int cat;

    @ColumnInfo(name = "description")
    String description;

    @ColumnInfo(name = "tagline")
    String tagline;

    @ColumnInfo(name = "image")
    String image;

    @ColumnInfo(name = "opentime")
    String opentime;

    @ColumnInfo(name = "closetime")
    String closetime;

    @ColumnInfo(name = "weekoff")
    String weekoff;

    @ColumnInfo(name = "address1")
    String address1;

    @ColumnInfo(name = "address2")
    String address2;

    @ColumnInfo(name = "country")
    int country;

    @ColumnInfo(name = "city")
    String city;

    @ColumnInfo(name = "state")
    int state;

    @ColumnInfo(name = "pin")
    String pin;

    @ColumnInfo(name = "web")
    String web;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "mobile")
    String mobile;

    @ColumnInfo(name = "telephone")
    String telephone;

    @ColumnInfo(name = "document")
    String document;

    @ColumnInfo(name = "userid")
    int userid;

    @ColumnInfo(name = "neighbrhood")
    String neighbrhood;

//    @ColumnInfo(name = "editid")
//    String editid;


    public BusinessModel(String businessname, int cat, String description, String tagline, String image, String opentime, String closetime, String weekoff,
                         String address1, String address2, int country, String city, int state, String pin, String web, String email, String mobile, String telephone, String document, int userid, String neighbrhood) {

        this.businessname = businessname;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public int getCat() {
        return cat;
    }

    public void setCat(int cat) {
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

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNeighbrhood() {
        return neighbrhood;
    }

    public void setNeighbrhood(String neighbrhood) {
        this.neighbrhood = neighbrhood;
    }

}
