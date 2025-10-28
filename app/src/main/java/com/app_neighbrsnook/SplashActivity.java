package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.registration.LastPageUserDocumentRegisteration;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.PreferenceManager;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Optional: Facebook debug key hash (for setup only)
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("FB_KEYHASH", "Key Hash: " + keyHash);
            }
        } catch (Exception e) {
            Log.e("FB_KEYHASH", "Error: ", e);
        }

        checkFirstInstallEvent();

        handleDeepLink(getIntent());

        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (PrefMananger.GetLoginData(SplashActivity.this) != null) {
                    if (PrefMananger.getScreen(SplashActivity.this).equals(PrefMananger.LITTLE_MORE)) {
                        startActivity(new Intent(SplashActivity.this, LastPageUserDocumentRegisteration.class));
                    } else if (PrefMananger.getScreen(SplashActivity.this).equals(PrefMananger.MAIN_ACTIVITY)) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, secondsDelayed * 1000);

    }

    private void checkFirstInstallEvent() {
        SharedPreferences prefs = getSharedPreferences("NeighbrsNookPrefs", MODE_PRIVATE);
        boolean hasLaunchedBefore = prefs.getBoolean("hasLaunchedBefore", false);

        if (!hasLaunchedBefore) {
            // Get dynamic values

            logFacebookInstallEvent();

//            Toast.makeText(this, "🎉 App Installed Successfully!", Toast.LENGTH_SHORT).show();

            prefs.edit().putBoolean("hasLaunchedBefore", true).apply();
        } else {
            Log.d("SplashActivity", "App reopened (not first install)");
        }
    }

    private void logFacebookInstallEvent() {
        try {
            AppEventsLogger logger = AppEventsLogger.newLogger(this);
            Bundle params = new Bundle();
            params.putString("event_name", "app_install_successfully_android_main");
            params.putString("platform", "Android");
            params.putString("device_model", android.os.Build.MODEL);
            params.putString("method", "first_app_install_event");

            logger.logEvent("app_install_successfully_android_main", params);
            logger.flush();

            Log.d("FB_Analytics", "📊 Facebook Install Event Sent: app_install_successfully_android");
            for (String key : params.keySet()) {
                Log.d("FB_Analytics", key + " = " + params.get(key));
            }

        } catch (Exception e) {
            Log.e("FB_Analytics", "❌ Failed to log Facebook install event: " + e.getMessage());
        }
    }

    private void handleDeepLink(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String path = data.getPath(); // /open-app
            String step = data.getQueryParameter("step"); // Get ?step=2

            if ("/open-app".equals(path)) {
//                Toast.makeText(this, "Step: " + step, Toast.LENGTH_SHORT).show();

                // You can navigate based on step
                if ("2".equals(step)) {
                    // Navigate to specific screen or show specific fragment
                }
            }
        }
    }
}