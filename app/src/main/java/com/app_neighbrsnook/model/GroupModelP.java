package com.app_neighbrsnook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groupModel")
public class GroupModelP {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "group_title")
    String group_title;

    @ColumnInfo(name = "about_group")
    String about_group;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }

    public String getAbout_group() {
        return about_group;
    }

    public void setAbout_group(String about_group) {
        this.about_group = about_group;
    }


}
