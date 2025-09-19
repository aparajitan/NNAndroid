package com.app_neighbrsnook.model;

public class SellModel {
    public static final int POPULAR_CAT = 1;
    public static final int TODAY_LIST = 2;
    public static final int WISH_LIST = 3;

    private int mType;
    String name;
    String product_name;
    String time;
    String distance;
    int image;
    int type;
    String price;
    String brand;

    public SellModel(int mType, String name, String product_name, String time, String distance, int image, int type, String price, String brand) {
        this.mType = mType;
        this.name = name;
        this.product_name = product_name;
        this.time = time;
        this.distance = distance;
        this.image = image;
        this.type = type;
        this.price = price;
        this.brand = brand;
    }

//    public SellModel(int mType, String name, String product_name, String time, String distance, int image, int type, String price) {
//        this.mType = mType;
//        this.name = name;
//        this.product_name = product_name;
//        this.time = time;
//        this.distance = distance;
//        this.image = image;
//        this.type = type;
//        this.price = price;
//
//    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
