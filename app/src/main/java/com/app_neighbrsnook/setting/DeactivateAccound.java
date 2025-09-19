package com.app_neighbrsnook.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.UtilityFunction;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeactivateAccound extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton days_30,days_60,days_90_days;
    Context context;
    ImageView img_back;
    Activity activity;
    String publish_nearby="";
    FrameLayout btn_deactivate;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=activity=this;
        setContentView(R.layout.activity_deactivate_accound);
        days_30=findViewById(R.id.thrity_days);
        days_60=findViewById(R.id.sixty_days);
        days_90_days=findViewById(R.id.ninety_days);
        radioGroup=findViewById(R.id.radio_group_id);
        btn_deactivate=findViewById(R.id.btn_deactivate);
        img_back=findViewById(R.id.img_back);
        btn_deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   deactivateAccountUser();
                exitLayout();
            }
        });

img_back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onBackPressed();
    }
});
    }
    private void deactivateAccountUser() {
        if (!UtilityFunction.isNetworkConnected(context)) {
            showToast("Network is not available");
            return;
        }

        UtilityFunction.showLoading(context, "Please wait...");

        // Prepare request parameters
        HashMap<String, Object> hashMap = createRequestParameters();

        // Make API call
        ApiExecutor.getApiService()
                .deactivateAccount("deactiveaccount", hashMap)
                .enqueue(new Callback<AddressResponse>() {
                    @Override
                    public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                        UtilityFunction.hideLoading();
                        handleApiResponse(response);
                    }
                    @Override
                    public void onFailure(Call<AddressResponse> call, Throwable t) {
                        UtilityFunction.hideLoading();
                        Log.e("DeactivateAccountError", t.getMessage());
                    }
                });
    }
    // Helper method to create request parameters
    private HashMap<String, Object> createRequestParameters() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", PrefMananger.GetLoginData(context).getId() + "");

        if (days_30.isChecked()) {
            hashMap.put("days", "30");
        } else if (days_60.isChecked()) {
            hashMap.put("days", "60");
        } else if (days_90_days.isChecked()) {
            hashMap.put("days", "90");
        }
        hashMap.put("status", "success");
        return hashMap;
    }
    // Helper method to handle API response
    private void handleApiResponse(Response<AddressResponse> response) {
        try {
            if (response.body() != null && "success".equalsIgnoreCase(response.body().getStatus())) {
                PrefMananger.SaveLoginData(getApplicationContext(),null);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finishAffinity();
                navigateToLogin();
            } else {
                showToast(response.body() != null ? response.body().getMessage() : "Something went wrong!");
                navigateToLogin(); // Optional: If required, navigate for failure as well
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("An error occurred!");
        }
    }

    // Helper method to navigate to LoginActivity
    private void navigateToLogin() {
        Intent intent = new Intent(DeactivateAccound.this, LoginActivity.class);
        startActivity(intent);
    }

    // Helper method to show Toast
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public void exitLayout(){
        Dialog dialog = new Dialog(DeactivateAccound.this);
        dialog.setContentView(R.layout.group_exit_request);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(DeactivateAccound.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no=dialog.findViewById(R.id.no_id);
        TextView tv_yes=dialog.findViewById(R.id.yes_id);
        TextView tvExitOne=dialog.findViewById(R.id.exitOne);
        TextView tvExitTwo=dialog.findViewById(R.id.exitTwo);
        tvExitTwo.setVisibility(View.GONE);
        tvExitOne.setText("Are you sure you want to deactivate your account?");
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deactivateAccountUser();
                dialog.dismiss();

            }

        });
    }
}