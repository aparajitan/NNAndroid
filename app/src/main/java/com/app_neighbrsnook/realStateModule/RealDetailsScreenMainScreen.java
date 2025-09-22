package com.app_neighbrsnook.realStateModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;

public class RealDetailsScreenMainScreen extends AppCompatActivity {
    ImageView img_back;
    TextView frm_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_details_screen_main_screen);
        img_back=findViewById(R.id.img_address_proof_back);
        frm_review=findViewById(R.id.review_id_details);
        frm_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealDetailsScreenMainScreen.this,ReviewShowActivity.class));
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}