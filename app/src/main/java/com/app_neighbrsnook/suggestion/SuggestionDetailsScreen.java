package com.app_neighbrsnook.suggestion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.profile.MyProfile;

import java.util.Locale;
public class SuggestionDetailsScreen extends AppCompatActivity {
    ImageView img_plus_dialog,img_back;
    Context context;
    Activity activity;
    LinearLayout lnr_direction,lnr_suggested_ngh;
    LinearLayout l1,l2,l3,call_dialog;
    FrameLayout frm_visit_web;
    ImageView tv_call_direct;
    ImageView img_add;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_suggestion_details_screen);
        img_plus_dialog=findViewById(R.id.img_plus_ic_yellow);
        img_back=findViewById(R.id.img_back);
        lnr_direction=findViewById(R.id.lnr_direction);
        l1=findViewById(R.id.lnr_first);
        l2=findViewById(R.id.lnr_second);
        l3=findViewById(R.id.lnr_third);
        img_add=findViewById(R.id.suggestion_create_pluse);
        tv_call_direct=findViewById(R.id.calling_no);
        lnr_suggested_ngh=findViewById(R.id.lnr_suggested_by_ngh);
        frm_visit_web=findViewById(R.id.visit_website);
        call_dialog=findViewById(R.id.lnr_calling);
        tv_call_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(SuggestionDetailsScreen.this);
                dialog.setContentView(R.layout.calling_dialog_pop_up);
                WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.gravity=Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SuggestionDetailsScreen.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(layoutParams);
                dialog.show();
                TextView tv_call=dialog.findViewById(R.id.call_id);
                TextView tv_cancel=dialog.findViewById(R.id.cancel_id);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:8126177819")));

                    }
                });
            }
        });

        frm_visit_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.anantatek.com/"));
            startActivity(intent);
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionDetailsScreen.this, MyProfile.class);
                startActivity(intent);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionDetailsScreen.this, MyProfile.class);
                startActivity(intent);
            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuggestionDetailsScreen.this,MakeSuggestion.class));
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SuggestionDetailsScreen.this, MyProfile.class);
                startActivity(intent);
            }
        });
        lnr_suggested_ngh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(SuggestionDetailsScreen.this);
                dialog.setContentView(R.layout.suggestion_made_by);
                WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.gravity=Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SuggestionDetailsScreen.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(layoutParams);
                dialog.show();
                ImageView img_cancel=dialog.findViewById(R.id.ic_cancel);
                img_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        lnr_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 28.7041, 77.1025);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_plus_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(SuggestionDetailsScreen.this);
                dialog.setContentView(R.layout.suggestion_details_pop_up_plus);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SuggestionDetailsScreen.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                ImageView  img=dialog.findViewById(R.id.iv9);
                FrameLayout  tv_post=dialog.findViewById(R.id.post_id);
                tv_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}