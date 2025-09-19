package com.app_neighbrsnook.realStateModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;

public class RealStateLandmarkLocality extends AppCompatActivity {
    ImageView img_back_arrow;
    FrameLayout frm_okhla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_state_landmark_locality);
        img_back_arrow=findViewById(R.id.back_arrow_id);
        frm_okhla=findViewById(R.id.frm_okhla_id);
        frm_okhla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateLandmarkLocality.this,RealStateFilterPg.class));
            }
        });
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}