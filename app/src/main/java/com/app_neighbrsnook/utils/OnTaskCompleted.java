package com.app_neighbrsnook.utils;

public interface OnTaskCompleted<T> {
    void onTaskCompleted(String response,int status_code);
}
