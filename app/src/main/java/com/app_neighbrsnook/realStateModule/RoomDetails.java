package com.app_neighbrsnook.realStateModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;

public class RoomDetails extends AppCompatActivity {
    ImageView img_back;
    LinearLayout lnr_furnished;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        img_back=findViewById(R.id.back_arrow_write_review);
        lnr_furnished=findViewById(R.id.lnr_furnished);
        lnr_furnished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RoomDetails.this,AddFurnishing.class));
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