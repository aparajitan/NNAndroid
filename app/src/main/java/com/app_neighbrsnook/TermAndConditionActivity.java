package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class TermAndConditionActivity extends AppCompatActivity {
    WebView webView;
    ImageView back_btn, search_imageview, add_imageview;
    TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        webView = findViewById(R.id.web1);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        search_imageview = findViewById(R.id.search_imageview);
        add_imageview = findViewById(R.id.add_imageview);

        search_imageview.setVisibility(View.GONE);
        add_imageview.setVisibility(View.GONE);
        titleTv.setText("Privacy policy");

        webView.loadUrl("http://neighbrsnook.com/privacy-policy/");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}

