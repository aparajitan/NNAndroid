package com.app_neighbrsnook.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app_neighbrsnook.AboutUsActivity;
import com.app_neighbrsnook.FaqActivity;
import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.TermAndConditionActivity;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.event.UserViewEvent;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.SettingPojo;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingAcitivity extends AppCompatActivity {
    FrameLayout frm_change_password, frm_deactivate_ac;
    ImageView back_arrow;
    FrameLayout frm_logout;
    Context context;
    Activity activity;
    RadioGroup rg_emaild_addres;
    RadioButton email_rb_hide,email_rb_show,rb_phone_hide,rb_phone_show,rb_emergency_hide,rb_emergency_show,rb_add_line_hide,rb_add_line_show,address_two_hide,address_two_show;
    FrameLayout frm_save_setting;
    String email_address="";
    String st_phone_no="";
    String st_address_line_two="";
    String st_emergency_phone_no="";
    String st_address_line="";
    String st_poll="";
    String st_poll_mail="";
    String st_comment_phone="";
    String st_phone_poll="";
    String st_phone_email="";
    String st_comment_mail,st_dm_phone,st_dm_mail,st_group_phone,st_group_mail,st_business_phone,st_business_mail;
    SharedPrefsManager sm;
    CheckBox ch_poll_phone,ch_poll_mail,ch_phone_poll,ch_email_poll;
    CheckBox ch_comment_phone,ch_comment_mail,dm_phone,dm_mail,group_phone,group_mail,business_phone,businee_mail;
    FrameLayout tvAboutUs,tvPrivacyPolicy,tvFaq,frmProfileVisibility,frmAcManage;
    LinearLayout lnrLayoutGone,downIv;
    boolean show= true;
    ImageView upIv;



    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_setting_acitivity);
        sm = new SharedPrefsManager(this);
        settingProfileApi();
        frm_change_password = findViewById(R.id.change_password_id);
        frmProfileVisibility = findViewById(R.id.rightArrow);
        frmAcManage = findViewById(R.id.frmAcManage);

        back_arrow = findViewById(R.id.img_back);
        ch_poll_phone=findViewById(R.id.poll_phone);
        ch_phone_poll=findViewById(R.id.phone_poll);
        ch_email_poll=findViewById(R.id.mail_poll);
        tvAboutUs=findViewById(R.id.tvAboutUs);
        tvFaq=findViewById(R.id.tvFaq);
        tvPrivacyPolicy=findViewById(R.id.tvPrivacyPolicy);
        business_phone=findViewById(R.id.business_phone);
        businee_mail=findViewById(R.id.business_mail);
        ch_poll_mail=findViewById(R.id.poll_mail);
        frm_deactivate_ac = findViewById(R.id.deactivate_accound_id);
        frm_logout = findViewById(R.id.frm_logout);
        rg_emaild_addres=findViewById(R.id.email_address_rg);
        email_rb_hide=findViewById(R.id.email_rb_hide);
        email_rb_show=findViewById(R.id.email_rb_show);
        frm_save_setting=findViewById(R.id.btn_save_setting);
        rb_phone_hide=findViewById(R.id.phone_rb_hide);
        rb_phone_show=findViewById(R.id.phone_rb_show);
        rb_emergency_hide=findViewById(R.id.emergency_rb_hide);
        rb_emergency_show=findViewById(R.id.emergency_rb_show);
        rb_add_line_hide=findViewById(R.id.address_line_rb_hide);
        rb_add_line_show=findViewById(R.id.address_line_rb_show);
        ch_comment_phone=findViewById(R.id.comment_phone);
        ch_comment_mail=findViewById(R.id.comment_mail);
        dm_phone=findViewById(R.id.dm_phone);
        dm_mail=findViewById(R.id.dm_mail);
        group_phone=findViewById(R.id.group_phone);
        group_mail=findViewById(R.id.group_mail);
        address_two_hide=findViewById(R.id.address_line2_hide);
        address_two_show=findViewById(R.id.address_line2_show);
        lnrLayoutGone=findViewById(R.id.layoutGone);
        downIv =findViewById(R.id.down_iv);
        upIv =findViewById(R.id.up_iv);
        downIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingAcitivity.this,NotificationSettingActivity.class));

            }
        });
        frmProfileVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingAcitivity.this,SettingVisibilityActivity.class));

            }
        });
        frmAcManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingAcitivity.this,SettingAccountManagement.class));

            }
        });

        frm_deactivate_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingAcitivity.this,DeactivateAccound.class));
                /*Dialog dialog = new Dialog(SettingAcitivity.this);
                dialog.setContentView(R.layout.deactivate_account_layouot);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SettingAcitivity.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                ImageView img_close = dialog.findViewById(R.id.iv9);
                TextView text_cancel = dialog.findViewById(R.id.tv_cancel_dialog);
                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                text_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });*/
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        frm_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingAcitivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        frm_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 logoutDialog();
            }
        });
        frm_save_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettingUser();
            }
        });
        tvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingAcitivity.this, FaqActivity.class));
            }
        });
        tvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingAcitivity.this, AboutUsActivity.class));
            }
        });
        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingAcitivity.this, TermAndConditionActivity.class));
            }
        });
    }

    public void logoutDialog() {
        Dialog dialog = new Dialog(SettingAcitivity.this);
        dialog.setContentView(R.layout.logout_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SettingAcitivity.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.no_id);
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
        ImageView img_cancel = dialog.findViewById(R.id.iv9);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                PrefMananger.SaveLoginData(getApplicationContext(),null);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finishAffinity();
            }
        });

    }

    private void saveSettingUser(){
        if (UtilityFunction.isNetworkConnected(context)) {
            UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("userid", PrefMananger.GetLoginData(context).getId()+"");
            if(email_rb_hide.isChecked()) {
                email_address= "0";
                hashMap.put("emailAddress", ( "0"));
            } if(email_rb_show.isChecked()) {
                email_address= "1";
                hashMap.put("emailAddress", ( "1"));
            } if(rb_phone_hide.isChecked()) {
                st_phone_no= "0";
                hashMap.put("phoneno", ( "0"));
            } if(rb_phone_show.isChecked()) {
                st_phone_no= "1";
                hashMap.put("phoneno", ( "1"));
            }
            if(rb_emergency_hide.isChecked()) {
                st_emergency_phone_no= "0";
                hashMap.put("emergencyContactno", ( "0"));
            } if(rb_emergency_show.isChecked()) {
                st_emergency_phone_no= "1";
                hashMap.put("emergencyContactno", ( "1"));
            }
            if(rb_add_line_hide.isChecked()) {
                st_address_line= "0";
                hashMap.put("addresslineone", ( "0"));
            } if(rb_add_line_show.isChecked()) {
                st_address_line= "1";
                hashMap.put("addresslineone", ( "1"));
            }
            if(address_two_hide.isChecked()) {
                st_address_line_two= "0";
                hashMap.put("addresslinetwo", ( "0"));
            } if(address_two_show.isChecked()) {
                st_address_line_two= "1";
                hashMap.put("addresslinetwo", ( "1"));
            }
            if(ch_poll_phone.isChecked()) {
                st_poll= "1";
                hashMap.put("notificationForEventMb", ( "1"));
            } else  {
                ch_poll_phone.setChecked(false);
                st_poll= "0";
                hashMap.put("notificationForEventMb", ( "0"));
            }
            if(ch_poll_mail.isChecked()) {
                st_poll_mail= "1";
                hashMap.put("notificationeventmail", ( "1"));
            } else  {
                ch_poll_mail.setChecked(false);
                st_poll_mail= "0";
                hashMap.put("notificationeventmail", ( "0"));
            } if(ch_phone_poll.isChecked()) {
                st_phone_poll= "1";
                hashMap.put("pollForVoteMb", ( "1"));
            } else  {
                ch_phone_poll.setChecked(false);
                st_phone_poll= "0";
                hashMap.put("pollForVoteMb", ( "0"));
            }
            if(ch_email_poll.isChecked()) {
                st_poll_mail= "1";
                hashMap.put("pollvotemail", ( "1"));
            } else  {
                ch_email_poll.setChecked(false);
                st_poll_mail= "0";
                hashMap.put("pollvotemail", ( "0"));
            }

            if(ch_comment_phone.isChecked()) {
                st_comment_phone= "1";
                hashMap.put("commentOnYourPostsMb", ( "1"));
            } else  {
                ch_comment_phone.setChecked(false);
                st_comment_phone= "0";
                hashMap.put("commentOnYourPostsMb", ( "0"));
            }
            if(ch_comment_mail.isChecked()) {
                st_comment_mail= "1";
                hashMap.put("commentpostmail", ( "1"));
            } else  {
                ch_comment_mail.setChecked(false);
                st_comment_mail= "0";
                hashMap.put("commentpostmail", ( "0"));
            }
            if(dm_phone.isChecked()) {
                st_dm_phone= "1";
                hashMap.put("directMessageMb", ( "1"));
            } else  {
                dm_phone.setChecked(false);
                st_dm_phone= "0";
                hashMap.put("directMessageMb", ( "0"));
            }
            if(dm_mail.isChecked()) {
                st_dm_mail= "1";
                hashMap.put("directmsgmail", ( "1"));
            } else  {
                dm_mail.setChecked(false);
                st_dm_mail= "0";
                hashMap.put("directmsgmail", ( "0"));
            }
            if(group_phone.isChecked()) {
                st_group_phone= "1";
                hashMap.put("newGroupMb", ( "1"));
            } else  {
                group_phone.setChecked(false);
                st_group_phone= "0";
                hashMap.put("newGroupMb", ( "0"));
            }
            if(group_mail.isChecked()) {
                st_group_mail= "1";
                hashMap.put("groupcreatemail", ( "1"));
            } else  {
                group_mail.setChecked(false);
                st_group_mail= "0";
                hashMap.put("groupcreatemail", ( "0"));
            }
            if(business_phone.isChecked()) {
                st_business_phone= "1";
                hashMap.put("rating_reviewsOnYourBusinessMb", ( "1"));
            } else  {
                business_phone.setChecked(false);
                st_business_phone= "0";
                hashMap.put("rating_reviewsOnYourBusinessMb", ( "0"));
            }
            if(businee_mail.isChecked()) {
                st_business_mail= "1";
                hashMap.put("rating_commentbusinessmail", ( "1"));
            } else  {
                businee_mail.setChecked(false);
                st_business_mail= "0";
                hashMap.put("rating_commentbusinessmail", ( "0"));
            }
            hashMap.put("status","success");
            ApiExecutor.getApiService().deactivateAccount("usermobilesetting",hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        // Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getStatus()=="success"){
                            UtilityFunction.hideLoading();
                        }else {

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    UtilityFunction.hideLoading();
                }
            });
        }else {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }
    private void settingProfileApi() {
        HashMap<String, Object> hm = new HashMap<>();
       // hm.put("groupid", id);
        //hm.put("userid", Integer.parseInt((PrefMananger.GetLoginData(context).getId() +"")));
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<SettingPojo> call = service.settingData("usermobilesetting", hm);
        call.enqueue(new Callback<SettingPojo>() {
            @Override
            public void onResponse(Call<SettingPojo> call, Response<SettingPojo> response) {
                SettingPojo rootObject= response.body();
                //JsonObject rootObject = businessDetailPojo.getAsJsonObject();
                try {
                   /* if (rootObject.getEmailAddress().equals("0")){
                        email_rb_show.setChecked(false);
                        email_rb_hide.setChecked(true);

                        //rb_anyone.setChecked(false);
                    } else {
                        email_rb_hide.setChecked(false);
                        email_rb_show.setChecked(true);

                        //rb_anyone.setChecked(true);
                    }*/if (rootObject.getAddresslineone().equals("0")){
                        rb_add_line_show.setChecked(false);
                        rb_add_line_hide.setChecked(true);

                        //rb_anyone.setChecked(false);
                    } else {
                        rb_add_line_hide.setChecked(false);
                        rb_add_line_show.setChecked(true);
                        //rb_anyone.setChecked(true);
                    }
                    if (rootObject.getAddresslinetwo().equals("0")){
                        address_two_show.setChecked(false);
                        address_two_hide.setChecked(true);

                        //rb_anyone.setChecked(false);
                    } else {
                        address_two_hide.setChecked(false);
                        address_two_show.setChecked(true);

                        //rb_anyone.setChecked(true);
                    }if (rootObject.getEmergencyContactno().equals("0")){
                        rb_phone_show.setChecked(false);
                        rb_phone_hide.setChecked(true);

                        //rb_anyone.setChecked(false);
                    } else {
                        rb_phone_hide.setChecked(false);
                        rb_phone_show.setChecked(true);
                        //rb_anyone.setChecked(true);
                    }if (rootObject.getEmergencyContactno().equals("0")){
                        rb_emergency_show.setChecked(false);
                        rb_emergency_hide.setChecked(true);

                        //rb_anyone.setChecked(false);
                    } else {
                        rb_emergency_hide.setChecked(false);
                        rb_emergency_show.setChecked(true);
                    }
                    ch_poll_phone.setChecked(!rootObject.getNotificationForEventMb().equals("0"));
                    ch_poll_mail.setChecked(!rootObject.getNotificationeventmail().equals("0"));
                    ch_comment_phone.setChecked(!rootObject.getCommentOnYourPostsMb().equals("0"));
                    ch_comment_mail.setChecked(!rootObject.getCommentpostmail().equals("0"));
                    dm_phone.setChecked(!rootObject.getDirectMessageMb().equals("0"));
                    dm_mail.setChecked(!rootObject.getDirectmsgmail().equals("0"));
                    group_phone.setChecked(!rootObject.getNewGroupMb().equals("0"));
                    group_mail.setChecked(!rootObject.getGroupcreatemail().equals("0"));
                    business_phone.setChecked(!rootObject.getRatingReviewsOnYourBusinessMb().equals("0"));
                    businee_mail.setChecked(!rootObject.getRatingCommentbusinessmail().equals("0"));
                    ch_phone_poll.setChecked(!rootObject.getPollForVoteMb().equals("0"));
                    ch_email_poll.setChecked(!rootObject.getPollvotemail().equals("0"));

                }catch (Exception exception)
                {
                }

            }
            @Override
            public void onFailure(Call<SettingPojo> call, Throwable t) {

                if (t.toString().contains("timeout")){
                    Toast.makeText(SettingAcitivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                }else if (t.toString().contains("Unable to resolve host")){
                    Toast.makeText(SettingAcitivity.this, "No internet", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(SettingAcitivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                }
                Log.d("res", t.getMessage());
            }
        });

    }


}