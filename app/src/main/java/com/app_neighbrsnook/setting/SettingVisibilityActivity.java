package com.app_neighbrsnook.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app_neighbrsnook.AboutUsActivity;
import com.app_neighbrsnook.FaqActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.TermAndConditionActivity;
import com.app_neighbrsnook.apiService.ApiExecutor;
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

public class SettingVisibilityActivity extends AppCompatActivity {
    FrameLayout frm_change_password, frm_deactivate_ac;
    ImageView back_arrow;
    FrameLayout frm_logout;
    Context context;
    Activity activity;
    RadioGroup rg_emaild_addres;
    RadioButton email_rb_hide, email_rb_show, contactNoHide, contactNoShow, rb_phone_hide, rb_phone_show, rb_emergency_hide, rb_emergency_show, rb_add_line_hide, professionHide, professionShow, rb_add_line_show, address_two_hide, address_two_show;
    FrameLayout frm_save_setting;
    String email_address = "";
    String st_phone_no = "";
    String st_address_line_two = "";
    String st_emergency_phone_no = "";
    String st_address_line = "";
    String st_profesion = "";
    String st_poll = "";
    String st_poll_mail = "";
    String st_comment_phone = "";
    String st_phone_poll = "";
    String st_phone_email = "";
    String st_comment_mail, st_dm_phone, st_dm_mail, st_group_phone, st_group_mail, st_business_phone, st_business_mail, stContactNo;
    SharedPrefsManager sm;
    CheckBox ch_poll_phone, ch_poll_mail, ch_phone_poll, ch_email_poll;
    CheckBox ch_comment_phone, ch_comment_mail, dm_phone, dm_mail, group_phone, group_mail, business_phone, businee_mail;
    FrameLayout tvAboutUs, tvPrivacyPolicy, tvFaq;
    LinearLayout lnrLayoutGone, downIv;
    boolean show = true;
    ImageView upIv;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_setting_profile_visibility);

        sm = new SharedPrefsManager(this);

        item();
        settingProfileApi();


        downIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingVisibilityActivity.this, NotificationSettingActivity.class));

            }
        });

        frm_deactivate_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingVisibilityActivity.this, DeactivateAccound.class));

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
                Intent intent = new Intent(SettingVisibilityActivity.this, ChangePasswordActivity.class);
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
                startActivity(new Intent(SettingVisibilityActivity.this, FaqActivity.class));
            }
        });
        tvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingVisibilityActivity.this, AboutUsActivity.class));
            }
        });
        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingVisibilityActivity.this, TermAndConditionActivity.class));
            }
        });
    }

    private void item() {
        frm_change_password = findViewById(R.id.change_password_id);
        back_arrow = findViewById(R.id.img_back);
        ch_poll_phone = findViewById(R.id.poll_phone);
        ch_phone_poll = findViewById(R.id.phone_poll);
        ch_email_poll = findViewById(R.id.mail_poll);
        professionHide = findViewById(R.id.professionHide);
        professionShow = findViewById(R.id.professionShow);
        tvAboutUs = findViewById(R.id.tvAboutUs);
        tvFaq = findViewById(R.id.tvFaq);
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        business_phone = findViewById(R.id.business_phone);
        businee_mail = findViewById(R.id.business_mail);
        ch_poll_mail = findViewById(R.id.poll_mail);
        frm_deactivate_ac = findViewById(R.id.deactivate_accound_id);
        frm_logout = findViewById(R.id.frm_logout);
        rg_emaild_addres = findViewById(R.id.email_address_rg);
        email_rb_hide = findViewById(R.id.email_rb_hide);
        email_rb_show = findViewById(R.id.email_rb_show);
        frm_save_setting = findViewById(R.id.btn_save_setting);
        rb_phone_hide = findViewById(R.id.phone_rb_hide);
        rb_phone_show = findViewById(R.id.phone_rb_show);
        rb_emergency_hide = findViewById(R.id.emergency_rb_hide);
        rb_emergency_show = findViewById(R.id.emergency_rb_show);
        rb_add_line_hide = findViewById(R.id.address_line_rb_hide);
        rb_add_line_show = findViewById(R.id.address_line_rb_show);
        ch_comment_phone = findViewById(R.id.comment_phone);
        ch_comment_mail = findViewById(R.id.comment_mail);
        dm_phone = findViewById(R.id.dm_phone);
        dm_mail = findViewById(R.id.dm_mail);
        group_phone = findViewById(R.id.group_phone);
        group_mail = findViewById(R.id.group_mail);
        address_two_hide = findViewById(R.id.address_line2_hide);
        address_two_show = findViewById(R.id.address_line2_show);
        lnrLayoutGone = findViewById(R.id.layoutGone);
        contactNoHide = findViewById(R.id.contactNoHide);
        contactNoShow = findViewById(R.id.contactNoShow);
        downIv = findViewById(R.id.down_iv);
        upIv = findViewById(R.id.up_iv);
    }

    public void logoutDialog() {
        Dialog dialog = new Dialog(SettingVisibilityActivity.this);
        dialog.setContentView(R.layout.logout_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SettingVisibilityActivity.this, android.R.color.transparent)));
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
                PrefMananger.SaveLoginData(getApplicationContext(), null);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finishAffinity();
            }
        });

    }

    private void saveSettingUser() {
        if (UtilityFunction.isNetworkConnected(context)) {
            UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", PrefMananger.GetLoginData(context).getId() + "");
            if (email_rb_hide.isChecked()) {
                email_address = "0";
                hashMap.put("emailAddress", ("0"));
            }
            if (email_rb_show.isChecked()) {
                email_address = "1";
                hashMap.put("emailAddress", ("1"));
            }
            if (contactNoHide.isChecked()) {
                st_phone_no = "0";
                hashMap.put("contactNo", ("0"));
            }
            if (contactNoShow.isChecked()) {
                st_phone_no = "1";
                hashMap.put("contactNo", ("1"));
            }
            if (rb_emergency_hide.isChecked()) {
                st_emergency_phone_no = "0";
                hashMap.put("emergencyContactno", ("0"));
            }
            if (rb_emergency_show.isChecked()) {
                st_emergency_phone_no = "1";
                hashMap.put("emergencyContactno", ("1"));
            }
            if (rb_add_line_hide.isChecked()) {
                st_address_line = "0";
                hashMap.put("address", ("0"));
            }
            if (rb_add_line_show.isChecked()) {
                st_address_line = "1";
                hashMap.put("address", ("1"));
            }
//            if (address_two_hide.isChecked()) {
//                st_address_line_two = "0";
//                hashMap.put("addresslinetwo", ("0"));
//            }
//            if (address_two_show.isChecked()) {
//                st_address_line_two = "1";
//                hashMap.put("addresslinetwo", ("1"));
//            }
            if (professionHide.isChecked()) {
                st_profesion = "0";
                hashMap.put("profession", ("0"));
            }
            if (professionShow.isChecked()) {
                st_profesion = "1";
                hashMap.put("profession", ("1"));
            }


            hashMap.put("status", "success");
            ApiExecutor.getApiService().deactivateAccount("usermobilesetting", hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        if (response.body().getStatus() == "success") {
                            UtilityFunction.hideLoading();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    UtilityFunction.hideLoading();
                }
            });
        } else {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void settingProfileApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<SettingPojo> call = service.settingData("usermobilesetting", hm);
        call.enqueue(new Callback<SettingPojo>() {
            @Override
            public void onResponse(Call<SettingPojo> call, Response<SettingPojo> response) {
                SettingPojo rootObject = response.body();
                try {
                    if (rootObject.getContactNo().equals("0")) {
                        contactNoShow.setChecked(false);
                        contactNoHide.setChecked(true);
                    } else if (rootObject.getContactNo().equals("1")) {
                        contactNoHide.setChecked(false);
                        contactNoShow.setChecked(true);
                    }

                    if (rootObject.getAddress().equals("0")) {
                        rb_add_line_show.setChecked(false);
                        rb_add_line_hide.setChecked(true);
                    } else {
                        rb_add_line_hide.setChecked(false);
                        rb_add_line_show.setChecked(true);
                    }

//                    if (rootObject.getAddresslinetwo().equals("0")) {
//                        address_two_show.setChecked(false);
//                        address_two_hide.setChecked(true);
//                    } else {
//                        address_two_hide.setChecked(false);
//                        address_two_show.setChecked(true);
//                    }

                    if (rootObject.getEmergencyContactno().equals("0")) {
                        rb_phone_show.setChecked(false);
                        rb_phone_hide.setChecked(true);
                    } else {
                        rb_phone_hide.setChecked(false);
                        rb_phone_show.setChecked(true);
                    }

                    if (rootObject.getEmergencyContactno().equals("0")) {
                        rb_emergency_show.setChecked(false);
                        rb_emergency_hide.setChecked(true);
                    } else {
                        rb_emergency_hide.setChecked(false);
                        rb_emergency_show.setChecked(true);
                    }

                    if (rootObject.getProfession().equals("0")) {
                        professionHide.setChecked(true);
                        professionShow.setChecked(false);
                    } else {
                        professionHide.setChecked(false);
                        professionShow.setChecked(true);
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

                } catch (Exception exception) {
                }

            }

            @Override
            public void onFailure(Call<SettingPojo> call, Throwable t) {

                if (t.toString().contains("timeout")) {
                    Toast.makeText(SettingVisibilityActivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                } else if (t.toString().contains("Unable to resolve host")) {
                    Toast.makeText(SettingVisibilityActivity.this, "No internet", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SettingVisibilityActivity.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();

                }
                Log.d("res", t.getMessage());
            }
        });

    }
}