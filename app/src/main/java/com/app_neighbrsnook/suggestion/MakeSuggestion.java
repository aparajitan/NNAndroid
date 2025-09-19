package com.app_neighbrsnook.suggestion;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.SuggestionTypeCategory;

public class MakeSuggestion extends AppCompatActivity implements SuggestionTypeCategory.CategoryInterface {
    FrameLayout frameLayout;
    ImageView img_back;
    TextView tv_suggestion_type;
    Dialog mail_dialog;
    ImageView img_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_suggestion);
        frameLayout=findViewById(R.id.post_id);
        img_back=findViewById(R.id.img_back);
        tv_suggestion_type=findViewById(R.id.business_category);
        img_search=findViewById(R.id.search_id_icon);
        img_search.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        });
        tv_suggestion_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void selectCategory() {
        RecyclerView rv;
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.open_category_list);
        rv = mail_dialog.findViewById(R.id.rv_category);
//        confirm = mail_dialog.findViewById(R.id.cross_imageview);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        SuggestionTypeCategory emailListAdapter = new SuggestionTypeCategory(mail_dialog, MakeSuggestion.this);
        rv.setAdapter(emailListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }
    @Override
    public void onClick(String categoryName) {
        tv_suggestion_type.setText(categoryName);
        categoryName = categoryName;
        mail_dialog.cancel();
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}