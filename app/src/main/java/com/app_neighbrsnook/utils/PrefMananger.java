package com.app_neighbrsnook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.app_neighbrsnook.pojo.LoginPojo;

public class PrefMananger {
    public static final String LOGIN_DATA = "LoginData";
    public static final String SCREEN = "Screen";
    public static final String LITTLE_MORE = "LittleMore";
    public static final String ADDRESS_PROOF = "AddressProof";
    public static final String ADDRESS_ID_PROOF = "AddressiDProof";
    public static final String MAIN_ACTIVITY = "MAINACTIVITY";
    private static final String PREF_NAME = "Neighbrsnook";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String MY_DATABASE = "Neighbrsnook";
    private static final String PREF_NAME1 = "location_permission_pref";
    private static final String KEY_GRANTED = "granted";
    private static final String KEY_DENIED_ONCE = "denied_once";

    public static void setGranted(Context context, boolean value) {
        context.getSharedPreferences(PREF_NAME1, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_GRANTED, value)
                .apply();
    }

    public static boolean isGranted(Context context) {
        return context.getSharedPreferences(PREF_NAME1, Context.MODE_PRIVATE)
                .getBoolean(KEY_GRANTED, false);
    }

    public static void setDeniedOnce(Context context, boolean value) {
        context.getSharedPreferences(PREF_NAME1, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_DENIED_ONCE, value)
                .apply();
    }

    public static boolean isDeniedOnce(Context context) {
        return context.getSharedPreferences(PREF_NAME1, Context.MODE_PRIVATE)
                .getBoolean(KEY_DENIED_ONCE, false);
    }


    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_DATABASE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }
 public static void saveLoginData(Context context, LoginPojo loginPojo) {
     SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
     SharedPreferences.Editor editor = sharedPreferences.edit();
     if (loginPojo != null) {
         // Convert object to JSON string
         String jsonData = new Gson().toJson(loginPojo);
         editor.putString("LoginData", jsonData);
         editor.putBoolean(IS_LOGGED_IN, true);
     } else {
         editor.remove("LoginData");
         editor.putBoolean(IS_LOGGED_IN, false);
     }
     editor.apply(); // Save changes
     // Log the updated values
     String loginData = sharedPreferences.getString("LoginData", null);
     boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
     Log.d("PrefManager", "LoginData: " + loginData + ", IsLoggedIn: " + isLoggedIn);
 }

    public static void clearLoginData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        // Logging to verify the cleared data
        String loginData = sharedPreferences.getString("LoginData", null);
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        Log.d("PrefManager", "After clearLoginData - LoginData: " + loginData + ", IsLoggedIn: " + isLoggedIn);
    }
    public static String getString(Context context, String key) {
        return context.getSharedPreferences(MY_DATABASE, Context.MODE_PRIVATE).getString(key, "");
    }
    public static void saveScreen(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_DATABASE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SCREEN, value).apply();
    }
    public static String getScreen(Context context) {
        return context.getSharedPreferences(MY_DATABASE, Context.MODE_PRIVATE).getString(SCREEN, "");
    }
    public static void SaveLoginData(Context context, LoginPojo loginPojo){
       if (loginPojo==null){
        saveString(context,LOGIN_DATA,"");
       }else {
           String json = new Gson().toJson(loginPojo);
           saveString(context,LOGIN_DATA,json);
       }
    }
    public static LoginPojo GetLoginData(Context context){
        String json=getString(context,LOGIN_DATA);
        Log.d("fdsafadfs",json);
        if (json.isEmpty()){
            return null;
        }else {
            LoginPojo loginPojo=new Gson().fromJson(json,LoginPojo.class);
            return loginPojo;
        }
    }
    public static void setVerified(Context context, String newValue) {
        // Retrieve the current LoginPojo object
        LoginPojo loginPojo = GetLoginData(context);

        if (loginPojo != null) {
            // Update the 'verified' value
            loginPojo.setVerified(newValue);

            // Save the updated LoginPojo object back to SharedPreferences
            SaveLoginData(context, loginPojo);
        } else {
            // If LoginPojo is null, create a new instance and set the 'verified' value
            loginPojo = new LoginPojo();
            loginPojo.setVerified(newValue);
            SaveLoginData(context, loginPojo);
        }
    }
}