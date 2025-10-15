package com.app_neighbrsnook;

import android.app.Application;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MyApplication extends Application {
    // Firebase Analytics ko yahaan rakh sakte hain, koi issue nahi hai.
    private static FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        // 🔹 1. Facebook SDK ko automatically initialize hone dein
        // AndroidManifest.xml mein "com.facebook.sdk.AutoInitEnabled" ko "true" kiya hai,
        // isliye yahaan manual initialization (sdkInitialize, activateApp) ki zaroorat nahi hai.
        // Ye purane methods hain aur ab deprecated ho chuke hain.

        // 🔹 2. Sirf Debugging ke liye logging enable karein (Optional, but recommended for testing)
        // Ye code production mein hata dein.

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token));
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        // ✅ 2. Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // ✅ 3. Optional: Log an event to confirm SDK working
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("fb_mobile_activate_app");

        // ✅ 4. Initialize Firebase Analytics (optional)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    // Getter for Firebase Analytics (optional utility)
    public static FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }
}


