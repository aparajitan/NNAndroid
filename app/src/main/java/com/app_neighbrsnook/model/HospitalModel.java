package com.app_neighbrsnook.model;

public class HospitalModel {
    String hospitalName;
    String address;
    double latitude;
    double longitude;

    public HospitalModel(String hospitalName, String address, double latitude, double longitude) {
        this.hospitalName = hospitalName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
