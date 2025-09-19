package com.app_neighbrsnook.model;

public class PopularModel {
    String name;
    int image;
    int bg_color;

    public PopularModel(String name, int image) {
        this.name = name;
        this.image = image;
    }
    public PopularModel(String name, int image, int bg_color) {
        this.name = name;
        this.image = image;
        this.bg_color = bg_color;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getBg_color() {
        return bg_color;
    }

    public void setBg_color(int bg_color) {
        this.bg_color = bg_color;
    }
}
