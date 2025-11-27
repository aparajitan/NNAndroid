package com.app_neighbrsnook.termsCondition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.app_neighbrsnook.R;

public class PrivacyPolicy extends AppCompatActivity {
    ImageView img_back;
    WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_condition);
        img_back=findViewById(R.id.img_back);
        webView1 = findViewById(R.id.web1);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webView1.loadUrl("http://neighbrsnook.com/privacy-policy/");
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new WebViewClient());
    }
}