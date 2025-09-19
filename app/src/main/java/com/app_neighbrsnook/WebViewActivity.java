package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {
    WebView webView,webView1;
    ImageView back_btn, search_imageview, add_imageview, back;

    TextView titleTv, member_number_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent i = getIntent();
        webView1 = findViewById(R.id.web1);
        titleTv = findViewById(R.id.title);
        back_btn= findViewById(R.id.back_btn);
        search_imageview = findViewById(R.id.search_imageview);
        add_imageview = findViewById(R.id.add_imageview);
        search_imageview.setVisibility(View.GONE);
        add_imageview.setVisibility(View.GONE);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText("Neighbrsnook");
        titleTv.setTextSize(14);

        member_number_tv = findViewById(R.id.member_number_tv);
        member_number_tv.setVisibility(View.VISIBLE);
        member_number_tv.setText(i.getStringExtra("companylink"));
        member_number_tv.setTextSize(10);
//        back_btn.setImageResource(R.drawable.clear_24);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        webView1.loadUrl(i.getStringExtra("companylink"));
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new WebViewClient());
    }
}