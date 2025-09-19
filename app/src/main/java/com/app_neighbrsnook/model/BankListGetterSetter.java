package com.app_neighbrsnook.model;

public class BankListGetterSetter {

    String BankName;
    public int BankImage;

    public BankListGetterSetter(String bankName, int bankImage) {
        BankName = bankName;
        BankImage = bankImage;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public int getBankImage() {
        return BankImage;
    }

    public void setBankImage(int bankImage) {
        BankImage = bankImage;
    }



}
