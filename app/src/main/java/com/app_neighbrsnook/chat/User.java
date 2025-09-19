package com.app_neighbrsnook.chat;

public class User {
    String userName;
    String name;
    String uid;

    public User(String userName, String name, String uid) {
        this.userName = userName;
        this.name = name;
        this.uid = uid;
    }

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
