package com.app_neighbrsnook.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Listdatum {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("tagline")
        public String tagline;

        @SerializedName("description")
        public String description;

        @SerializedName("neighborhood")
        public String neighborhood;

        @SerializedName("review")
        public Integer review;

        @SerializedName("rating")
        public String rating;

        @SerializedName("userid")
        public String userid;

        @SerializedName("userpic")
        public String userpic;

        @SerializedName("username")
        public String username;

        @SerializedName("web")
        public String web;

        @SerializedName("weekly_off")
        public String weeklyOff;

        @SerializedName("doc")
        public String doc;

        @SerializedName("opentime")
        public String opentime;

        @SerializedName("phone_no")
        public String phoneNo;

        @SerializedName("bisaddress")
        public String bisaddress;

        @SerializedName("state")
        public String state;

        @SerializedName("country")
        public String country;

        @SerializedName("pincode")
        public String pincode;

        @SerializedName("email")
        public String email;

        @SerializedName("tel")
        public String tel;

        @SerializedName("frtime")
        public String frtime;

        @SerializedName("clstime")
        public String clstime;

        @SerializedName("add1")
        public String add1;

        @SerializedName("add2")
        public String add2;

        @SerializedName("city")
        public String city;

        @SerializedName("category")
        public String category;

        @SerializedName("catid")
        public String catid;

        @SerializedName("businessstatus")
        public String businessstatus;

        @SerializedName("rating_status")
        public Integer ratingStatus;

        @SerializedName("reviewstatus")
        public Integer reviewstatus;

        @SerializedName("fastatus")
        public Integer fastatus;

        @SerializedName("image")
        public List<ImagePojo> image;

        @SerializedName("video")
        public List<Object> video;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getWeeklyOff() {
        return weeklyOff;
    }

    public void setWeeklyOff(String weeklyOff) {
        this.weeklyOff = weeklyOff;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBisaddress() {
        return bisaddress;
    }

    public void setBisaddress(String bisaddress) {
        this.bisaddress = bisaddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFrtime() {
        return frtime;
    }

    public void setFrtime(String frtime) {
        this.frtime = frtime;
    }

    public String getClstime() {
        return clstime;
    }

    public void setClstime(String clstime) {
        this.clstime = clstime;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getBusinessstatus() {
        return businessstatus;
    }

    public void setBusinessstatus(String businessstatus) {
        this.businessstatus = businessstatus;
    }

    public Integer getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(Integer ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public Integer getReviewstatus() {
        return reviewstatus;
    }

    public void setReviewstatus(Integer reviewstatus) {
        this.reviewstatus = reviewstatus;
    }

    public Integer getFastatus() {
        return fastatus;
    }

    public void setFastatus(Integer fastatus) {
        this.fastatus = fastatus;
    }

    public List<ImagePojo> getImage() {
        return image;
    }

    public void setImage(List<ImagePojo> image) {
        this.image = image;
    }

    public List<Object> getVideo() {
        return video;
    }

    public void setVideo(List<Object> video) {
        this.video = video;
    }
}
