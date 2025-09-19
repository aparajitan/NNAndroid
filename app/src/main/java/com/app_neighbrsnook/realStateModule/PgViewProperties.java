package com.app_neighbrsnook.realStateModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.app_neighbrsnook.R;

public class PgViewProperties extends AppCompatActivity {
    CardView card_one;
    ImageView img_back;
    LinearLayout lnr_sort;
    FrameLayout frm_listing,frm_any_price_click,frm_gender;
    CardView frm_l1,frm_l2;
    Boolean is_any_price=false;
    Boolean is_gender=false;
    LinearLayout lnr_filter_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_view_properties);
        card_one=findViewById(R.id.card_one_listing);
        img_back=findViewById(R.id.back_properties);
        lnr_sort=findViewById(R.id.lnr_sort_id);

        lnr_filter_layout=findViewById(R.id.lnr_filter_id);

        lnr_filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PgViewProperties.this);
                dialog.setContentView(R.layout.filter_layout_real_state);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PgViewProperties.this,
                        android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();

            }
        });

        lnr_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PgViewProperties.this);
                dialog.setContentView(R.layout.sort_layout_real_state);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PgViewProperties.this,
                        android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        card_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PgViewProperties.this,RealDetailsScreenMainScreen.class));
            }
        });
    }
}