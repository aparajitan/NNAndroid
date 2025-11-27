package com.app_neighbrsnook.nearBy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.WirteReviewActivity;

public class NearByReviewActivity extends AppCompatActivity {
    ImageView write_review_iv;
    ImageView back_btn,searchImageView, addImageView;
    TextView titleTv, business_review, report_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_review);
        write_review_iv = findViewById(R.id.write_review_iv);
        titleTv = findViewById(R.id.title);
        titleTv.setText("Near By");
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);

        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        write_review_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(NearByReviewActivity.this, WirteReviewActivity.class);
                startActivity(i);
            }
        });
    }
}