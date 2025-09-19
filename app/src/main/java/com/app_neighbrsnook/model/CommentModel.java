package com.app_neighbrsnook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "commentModel")
public class CommentModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "comment_msg")
    String comment_msg;

    @ColumnInfo(name = "comment_person")
    String comment_person;

    @ColumnInfo(name = "comment_time")
    String comment_time;

    @ColumnInfo(name = "comment_location")
    String comment_location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment_msg() {
        return comment_msg;
    }

    public void setComment_msg(String comment_msg) {
        this.comment_msg = comment_msg;
    }

    public String getComment_person() {
        return comment_person;
    }

    public void setComment_person(String comment_person) {
        this.comment_person = comment_person;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_location() {
        return comment_location;
    }

    public void setComment_location(String comment_location) {
        this.comment_location = comment_location;
    }
}
