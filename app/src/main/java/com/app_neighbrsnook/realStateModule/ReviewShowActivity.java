package com.app_neighbrsnook.realStateModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;

public class ReviewShowActivity extends AppCompatActivity {
    FrameLayout frm_society;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_show);
        frm_society=findViewById(R.id.review_your_society_id_frm);
        frm_society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewShowActivity.this,WriteReviewRealState.class));
            }
        });
    }
}