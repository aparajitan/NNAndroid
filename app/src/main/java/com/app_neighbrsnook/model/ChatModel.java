package com.app_neighbrsnook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatModel")
public class ChatModel {

    public static final int SENDER = 1;
    public static final int RECIVER = 2;
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "comment_msg")
    String comment_msg;

    @ColumnInfo(name = "sender_or_reciever")
    int sender_or_reciever;

    @ColumnInfo(name = "time")
    String time;

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

    public int getSender_or_reciever() {
        return sender_or_reciever;
    }

    public void setSender_or_reciever(int sender_or_reciever) {
        this.sender_or_reciever = sender_or_reciever;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
