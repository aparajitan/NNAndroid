package com.app_neighbrsnook;

import android.app.Application;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MyApplication extends Application {
    private static FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        // 🔹 1. Initialize Facebook SDK properly
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        // 🔹 (Optional) Debug logging for development
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        // 🔹 If you’re using a client token (from Meta App settings)
        try {
            FacebookSdk.setClientToken(getString(R.string.facebook_client_token));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔹 2. Initialize Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // 🔹 Log a sample Firebase event for app open
        Bundle bundle = new Bundle();
        bundle.putString("test_event", "App Opened");
        firebaseAnalytics.logEvent("app_open", bundle);

        // 🔹 Log a sample Facebook activation event
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("fb_mobile_activate_app");
    }

    // 🔹 Getter for Firebase Analytics
    public static FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }
}
