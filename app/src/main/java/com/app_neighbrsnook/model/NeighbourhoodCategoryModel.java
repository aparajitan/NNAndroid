package com.app_neighbrsnook.model;

public class NeighbourhoodCategoryModel {
    String place;
    String member;
//    String name;

    public NeighbourhoodCategoryModel(String place, String member) {
        this.place = place;
        this.member = member;

    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}
