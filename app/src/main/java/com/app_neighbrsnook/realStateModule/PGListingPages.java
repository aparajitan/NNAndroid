package com.app_neighbrsnook.realStateModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.app_neighbrsnook.R;

public class PGListingPages extends AppCompatActivity {
    LinearLayout lnr_filter_layout, frm_listing;
    CardView card_first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pglisting_pages);
        lnr_filter_layout = findViewById(R.id.lnr_filter_id);
        frm_listing = findViewById(R.id.lnr_sort_id);
        card_first = findViewById(R.id.card_first);
        card_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PGListingPages.this, RealDetailsScreenMainScreen.class));
            }
        });
        frm_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PGListingPages.this);
                dialog.setContentView(R.layout.sort_layout_real_state);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PGListingPages.this,
                        android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();

            }
        });
        lnr_filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PGListingPages.this);
                dialog.setContentView(R.layout.filter_layout_real_state);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PGListingPages.this,
                        android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();

            }
        });
    }
}