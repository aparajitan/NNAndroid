package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.registration.LastPageUserDocumentRegisteration;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int secondsDelayed = 2;
        //ImageView logo = findViewById(R.id.logoImageView);
   //     Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        //logo.startAnimation(animation);
        // Check for deep link
        handleDeepLink(getIntent());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (PrefMananger.GetLoginData(SplashActivity.this)!=null){
                    String screenName =  PrefMananger.getScreen(SplashActivity.this);
                    if (PrefMananger.getScreen(SplashActivity.this).equals(PrefMananger.LITTLE_MORE)) {
                        startActivity(new Intent(SplashActivity.this, LastPageUserDocumentRegisteration.class));
                    }
                    /*else  if (PrefMananger.getScreen(SplashActivity.this).equals(PrefMananger.ADDRESS_PROOF)) {
                        startActivity(new Intent(SplashActivity.this, AddressProof.class));
                    }else  if (PrefMananger.getScreen(SplashActivity.this).equals(PrefMananger.ADDRESS_ID_PROOF)) {
                        startActivity(new Intent(SplashActivity.this, AddressIdProof.class));
                    }*/else  if (PrefMananger.getScreen(SplashActivity.this).equals(PrefMananger.MAIN_ACTIVITY)) {
                        startActivity(new Intent( SplashActivity.this, MainActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, secondsDelayed * 1000);

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