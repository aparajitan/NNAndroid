package com.app_neighbrsnook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "suggestionModels")
public class SuggestionModel {

    @PrimaryKey(autoGenerate = true)
    int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBusiness_title() {
        return business_title;
    }
    public void setBusiness_title(String business_title) {
        this.business_title = business_title;
    }
    public String getAddress_type() {
        return address_type;
    }
    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }
    @ColumnInfo(name = "business_title")
    String business_title;
    @ColumnInfo(name = "address_type")
    String address_type;
}
