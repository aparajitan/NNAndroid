package com.app_neighbrsnook.model;

public class Country {

    String id;
    String state_name;

    public Country(String id, String state_name) {
        this.id = id;
        this.state_name = state_name;
    }

    @Override
    public String toString() {
        return state_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}





