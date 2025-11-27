package com.app_neighbrsnook.model;

public class TodayListModel {
    String name;
    String product_name;
    String time;
    String distance;
    int image;
    String type;



    public TodayListModel(String name, String product_name, String time, String distance, int image, String type) {
        this.name = name;
        this.product_name = product_name;
        this.time = time;
        this.distance = distance;
        this.image = image;
        this.type = type;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
