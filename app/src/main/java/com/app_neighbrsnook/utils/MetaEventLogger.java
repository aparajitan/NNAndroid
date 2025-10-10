package com.app_neighbrsnook.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.appevents.AppEventsConstants;

public class MetaEventLogger {



    /**
     * Logs a Facebook App Event for registration steps or other user actions.
     *
     * @param context   Context from Activity or Application
     * @param eventName Name of the Facebook predefined or custom event
     * @param stepName  Custom parameter for identifying registration step
     * @param userId    Optional user ID to link with event
     */
    public static void logEvent(Context context, String eventName, String stepName, String userId) {
        try {
            AppEventsLogger logger = AppEventsLogger.newLogger(context);
            Bundle params = new Bundle();

            params.putString("meta_registration_step", stepName);
            params.putString("meta_platform", "android_app");
            params.putString("meta_source", "user_registration");
            if (userId != null && !userId.isEmpty()) {
                params.putString("user_id", userId);
            }

            logger.logEvent(eventName, params);

            // Debug log (optional)
            Log.d("MetaEventLogger", "Event Logged: " + eventName + " | Step: " + stepName + " | UserID: " + userId);

        } catch (Exception e) {
            Log.e("MetaEventLogger", "Error logging event: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
