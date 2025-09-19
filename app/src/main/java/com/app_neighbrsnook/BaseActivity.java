package com.app_neighbrsnook;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

public class BaseActivity extends AppCompatActivity {
    protected FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔹 Firebase Analytics instance lein
        firebaseAnalytics = MyApplication.getFirebaseAnalytics();

        // 🔹 Current Activity ka naam track karein
        logScreenView();
    }

    private void logScreenView() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getClass().getSimpleName());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
}
