package com.app_neighbrsnook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pollModel")
public class PollModel {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "poll_title")
    String poll_title;

    @ColumnInfo(name = "poll_question")
    String poll_question;

    @ColumnInfo(name = "poll_option1")
    String poll_option1;

    @ColumnInfo(name = "poll_option2")
    String poll_option2;

    @ColumnInfo(name = "poll_option3")
    String poll_option3;

    @ColumnInfo(name = "poll_option4")
    String poll_option4;

    @ColumnInfo(name = "pool_start_date")
    String pool_start_date;

    @ColumnInfo(name = "pool_end_date")
    String pool_end_date;

    @ColumnInfo(name = "current_date_time")
    String current_date_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoll_title() {
        return poll_title;
    }

    public void setPoll_title(String poll_title) {
        this.poll_title = poll_title;
    }

    public String getPoll_question() {
        return poll_question;
    }

    public void setPoll_question(String poll_question) {
        this.poll_question = poll_question;
    }

    public String getPoll_option1() {
        return poll_option1;
    }

    public void setPoll_option1(String poll_option1) {
        this.poll_option1 = poll_option1;
    }

    public String getPoll_option2() {
        return poll_option2;
    }

    public void setPoll_option2(String poll_option2) {
        this.poll_option2 = poll_option2;
    }

    public String getPoll_option3() {
        return poll_option3;
    }

    public void setPoll_option3(String poll_option3) {
        this.poll_option3 = poll_option3;
    }

    public String getPoll_option4() {
        return poll_option4;
    }

    public void setPoll_option4(String poll_option4) {
        this.poll_option4 = poll_option4;
    }

    public String getPool_start_date() {
        return pool_start_date;
    }

    public void setPool_start_date(String pool_start_date) {
        this.pool_start_date = pool_start_date;
    }

    public String getPool_end_date() {
        return pool_end_date;
    }

    public void setPool_end_date(String pool_end_date) {
        this.pool_end_date = pool_end_date;
    }

    public String getCurrent_date_time() {
        return current_date_time;
    }

    public void setCurrent_date_time(String current_date_time) {
        this.current_date_time = current_date_time;
    }
}
