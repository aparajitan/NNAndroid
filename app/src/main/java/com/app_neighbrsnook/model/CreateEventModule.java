package com.app_neighbrsnook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "createModule")
public class CreateEventModule {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "event_title")
    String event_title;

    @ColumnInfo(name = "event_description")
    String event_description;

    @ColumnInfo(name = "address_line_one")
    String address_line_one;

    @ColumnInfo(name ="address_line_two")
    String address_line_two;

    @ColumnInfo(name = "event_start_date")
    String event_start_date;

    @ColumnInfo(name = "event_end_date")
    String event_end_date;


    public String getEvent_start_date() {
        return event_start_date;
    }

    public void setEvent_start_date(String event_start_date) {
        this.event_start_date = event_start_date;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public void setEvent_end_date(String event_end_date) {
        this.event_end_date = event_end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getAddress_line_one() {
        return address_line_one;
    }

    public void setAddress_line_one(String address_line_one) {
        this.address_line_one = address_line_one;
    }

    public String getAddress_line_two() {
        return address_line_two;
    }

    public void setAddress_line_two(String address_line_two) {
        this.address_line_two = address_line_two;
    }

}
