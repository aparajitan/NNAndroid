package com.app_neighbrsnook.model;

public class DayModel {
    String day;
    private boolean isSelected;

    public DayModel(String day) {
        this.day = day;
    }

    public DayModel(String day, boolean isSelected) {
        this.day = day;
        this.isSelected = isSelected;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
