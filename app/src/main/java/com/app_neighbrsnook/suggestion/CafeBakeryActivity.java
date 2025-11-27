package com.app_neighbrsnook.suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;

public class CafeBakeryActivity extends AppCompatActivity {
    ImageView img_back;
    ImageView img_add_suggestion;
    LinearLayout lnr_parent_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_bakery);
        img_back=findViewById(R.id.iv43);
        lnr_parent_layout=findViewById(R.id.lnr_parent_layout_plumber);
        img_add_suggestion=findViewById(R.id.add_suggestion);
        lnr_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CafeBakeryActivity.this,SuggestionDetailsScreen.class));
            }
        });
        img_add_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CafeBakeryActivity.this,AddSuggestion.class));
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