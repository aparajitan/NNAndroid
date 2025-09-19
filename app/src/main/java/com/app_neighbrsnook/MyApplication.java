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

        // 🔹 Firebase Analytics Initialize Karein
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("test_event", "App Opened");
        firebaseAnalytics.logEvent("app_open", bundle);
        // 🔹 Facebook SDK Setup
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token));
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("fb_mobile_activate_app");
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
    }

    // 🔹 FirebaseAnalytics ka instance get karne ke liye ek method banayein
    public static FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }
}
