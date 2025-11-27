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
            params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, "email");
            params.putString(AppEventsConstants.EVENT_PARAM_SUCCESS, "1");
            params.putString("user_id", userId);
            // Additional custom parameters
            params.putString("registration_step", stepName);
            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params);
            logger.flush();
            Log.d("MetaEventLogger", "Event Logged: " + eventName + " | Step: " + stepName + " | UserID: " + userId);
        } catch (Exception e) {
            Log.e("MetaEventLogger", "Error logging event: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void testMetaEvents(Context context) {
        try {
            AppEventsLogger testLogger = AppEventsLogger.newLogger(context);

            // Test events log karein
            testLogger.logEvent("fb_mobile_activate_app");
            testLogger.logEvent("test_event");

            // Immediate flush karein
            testLogger.flush();

            Log.d("MetaEventLogger", "Test events sent successfully");

        } catch (Exception e) {
            Log.e("MetaEventLogger", "Test event error: " + e.getMessage());
        }
    }
}
