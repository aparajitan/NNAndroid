package com.app_neighbrsnook.model;


public class CityEvent {
    public static final int CITY_TYPE = 0;
    public static final int POLL_TYPE = 1;
    public static final int GROUP_TYPE = 3;
    public static final int WALL_TYPE = 4;
    public static final int EVENT_TYPE = 2;
    public static final int SPONSOR_TYPE = 5;
    public static final int ANNOUNCEMENT_TYPE= 6;


    private String mName;
    private int mType;
    private String mDescription;
    private String mTagline;
    private String mBusinessType;

    private String p_title;
    private String p_start_date;
    private String p_end_date;
    private String p_question;

    private String g_place;
    private String g_no_of_members;

    private int wall_image;


    public CityEvent(String mName, String mBusinessType, String mDescription, String mTagline, int mType,
                        String p_title, String p_start_date, String p_end_date, String p_question, String g_place, String g_no_of_members ) {
        this.mName = mName;
        this.mType = mType;
        this.mDescription = mDescription;
        this.mTagline = mTagline;
        this.mBusinessType = mBusinessType;

        this.p_title = p_title;
        this.p_start_date = p_start_date;
        this.p_end_date = p_end_date;
        this.p_question = p_question;

        this.g_place = g_place;
        this.g_no_of_members = g_no_of_members;
    }


    public CityEvent(String mName, String mBusinessType, String mDescription, String mTagline, int mType,
                     String p_title, String p_start_date, String p_end_date, String p_question, String g_place, String g_no_of_members, int wall_image ) {
        this.mName = mName;
        this.mType = mType;
        this.mDescription = mDescription;
        this.mTagline = mTagline;
        this.mBusinessType = mBusinessType;

        this.p_title = p_title;
        this.p_start_date = p_start_date;
        this.p_end_date = p_end_date;
        this.p_question = p_question;

        this.g_place = g_place;
        this.g_no_of_members = g_no_of_members;
        this.wall_image = wall_image;
    }



    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmTagline() {
        return mTagline;
    }

    public void setmTagline(String mTagline) {
        this.mTagline = mTagline;
    }

    public String getmBusinessType() {
        return mBusinessType;
    }

    public void setmBusinessType(String mBusinessType) {
        this.mBusinessType = mBusinessType;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public String getP_start_date() {
        return p_start_date;
    }

    public void setP_start_date(String p_start_date) {
        this.p_start_date = p_start_date;
    }

    public String getP_end_date() {
        return p_end_date;
    }

    public void setP_end_date(String p_end_date) {
        this.p_end_date = p_end_date;
    }

    public String getP_question() {
        return p_question;
    }

    public void setP_question(String p_question) {
        this.p_question = p_question;
    }

    public String getG_place() {
        return g_place;
    }

    public void setG_place(String g_place) {
        this.g_place = g_place;
    }

    public String getG_no_of_members() {
        return g_no_of_members;
    }

    public void setG_no_of_members(String g_no_of_members) {
        this.g_no_of_members = g_no_of_members;
    }

    public int getImage() {
        return wall_image;
    }

    public void setImage(int wall_image) {
        this.wall_image = wall_image;
    }
}


