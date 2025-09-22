package com.app_neighbrsnook.model;

public class BusinessListModel {
    int img;
    String distance;
    String name;
    String rating;
    String location;
    String review;

    public BusinessListModel(int img, String distance, String name, String rating, String location, String review) {
        this.img = img;
        this.distance = distance;
        this.name = name;
        this.rating = rating;
        this.location = location;
        this.review = review;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
