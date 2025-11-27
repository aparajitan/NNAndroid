package com.app_neighbrsnook.suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.R;

public class SuggestionActivity extends AppCompatActivity {
    ImageView img_add_suggestion,img_back;
    FrameLayout frm_cafe_bakery,frm_restaurants,frm_dry,frm_pet_care,frm_garden,frm_hospital,frm_plumber,
    frm_carpentar,frm_electrician,frm_school,frm_tuition,frm_groceries,frm_sallon,frm_fitness;
    LinearLayout lnr_carpentar_saventh_card;
    ImageView img_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        img_add_suggestion=findViewById(R.id.suggestion_create_pluse);
        frm_cafe_bakery=findViewById(R.id.id_cafe_baker);
        frm_restaurants=findViewById(R.id.restaurant_id);
        frm_dry=findViewById(R.id.dry_care_id);
        frm_pet_care=findViewById(R.id.pet_care_id);
        frm_garden=findViewById(R.id.garden_id);
        frm_hospital=findViewById(R.id.hospital_id);
        img_back=findViewById(R.id.img_back);
        lnr_carpentar_saventh_card=findViewById(R.id.carpentar_saventh_card);
        img_add=findViewById(R.id.search_id);
        lnr_carpentar_saventh_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuggestionActivity.this,CafeBakeryActivity.class));
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        frm_cafe_bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionActivity.this,CafeBakeryActivity.class);
                startActivity(intent);
            }
        });

        frm_restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SuggestionActivity.this,UserCreateSuggestionShow.class));

            }
        });
        frm_dry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionActivity.this,CafeBakeryActivity.class);
                startActivity(intent);
            }
        });
        frm_pet_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionActivity.this,CafeBakeryActivity.class);
                startActivity(intent);
            }
        });
        frm_garden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionActivity.this,CafeBakeryActivity.class);
                startActivity(intent);
            }
        });
        frm_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionActivity.this,CafeBakeryActivity.class);
                startActivity(intent);
            }
        });
        img_add_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionActivity.this,MakeSuggestion.class);
                startActivity(intent);
            }
        });
    }
}