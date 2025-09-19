package com.app_neighbrsnook.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.UtilityFunction;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangePasswordActivity extends AppCompatActivity {
    ImageView back_arrow, ivNewPassShow, ivNewPassHide, ivConfirmPassShow, ivConfirmPassHide;
    EditText et_old_pass, et_new_pass, et_reType_pass;
    Context context;
    FrameLayout btn_update_password;
    private TextView tvStrength;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_change_password);

        back_arrow = findViewById(R.id.img_back);
        et_old_pass = findViewById(R.id.old_password_edt);
        et_new_pass = findViewById(R.id.new_password_edt);
        et_reType_pass = findViewById(R.id.confirm_password_edt);
        btn_update_password = findViewById(R.id.post_id);
        tvStrength = findViewById(R.id.tvStrength);

        ivNewPassShow = findViewById(R.id.iv_new_pass_show);
        ivNewPassHide = findViewById(R.id.iv_new_pass_hide);
        ivConfirmPassShow = findViewById(R.id.iv_old_confirm_pass_show);
        ivConfirmPassHide = findViewById(R.id.iv_old_confirm_pass_hide);

        back_arrow.setOnClickListener(view -> onBackPressed());

        ivNewPassHide.setOnClickListener(v -> {
            et_new_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_new_pass.setSelection(et_new_pass.getText().length());
            ivNewPassHide.setVisibility(View.GONE);
            ivNewPassShow.setVisibility(View.VISIBLE);
        });

        ivNewPassShow.setOnClickListener(v -> {
            et_new_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_new_pass.setSelection(et_new_pass.getText().length());
            ivNewPassShow.setVisibility(View.GONE);
            ivNewPassHide.setVisibility(View.VISIBLE);
        });

        ivConfirmPassHide.setOnClickListener(v -> {
            et_reType_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_reType_pass.setSelection(et_reType_pass.getText().length());
            ivConfirmPassHide.setVisibility(View.GONE);
            ivConfirmPassShow.setVisibility(View.VISIBLE);
        });

        ivConfirmPassShow.setOnClickListener(v -> {
            et_reType_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_reType_pass.setSelection(et_reType_pass.getText().length());
            ivConfirmPassShow.setVisibility(View.GONE);
            ivConfirmPassHide.setVisibility(View.VISIBLE);
        });

        et_new_pass.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPasswordStrength(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btn_update_password.setOnClickListener(view -> {
            if (et_old_pass.getText().toString().isEmpty()) {
                et_old_pass.requestFocus();
                et_old_pass.setError("Please enter old password");
            } else if (et_new_pass.getText().toString().isEmpty()) {
                et_new_pass.requestFocus();
                et_new_pass.setError("Please enter new password");
            } else if (et_new_pass.getText().toString().length() < 5) {
                et_new_pass.requestFocus();
                et_new_pass.setError("Password must be at least 5 characters");
            } else if (et_reType_pass.getText().toString().isEmpty()) {
                et_reType_pass.requestFocus();
                et_reType_pass.setError("Please confirm password");
            } else if (!et_reType_pass.getText().toString().equals(et_new_pass.getText().toString())) {
                et_reType_pass.requestFocus();
                et_reType_pass.setError("The passwords don't match");
            } else {
                changePassword();
            }
        });
    }

    private void changePassword() {
        if (UtilityFunction.isNetworkConnected(context)) {
            UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("userid", PrefMananger.GetLoginData(context).getId() + "");
            hashMap.put("actualpassword", et_old_pass.getText().toString());//var
            hashMap.put("newpassword", et_new_pass.getText().toString());//var
            hashMap.put("confirm_password", et_reType_pass.getText().toString());
            hashMap.put("status", "success");
            ApiExecutor.getApiService().updateApiPassword("changepassword", hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, retrofit2.Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getStatus().equals("success")) {
                            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (response.body().getStatus().equals("failed")) {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void checkPasswordStrength(String password) {
        if (password.isEmpty()) {
            tvStrength.setText("");
            return;
        }

        int strengthLevel = getPasswordStrength(password);

        if (strengthLevel == 1) {
            tvStrength.setText("Weak");
            tvStrength.setTextColor(Color.RED);
        } else if (strengthLevel == 2) {
            tvStrength.setText("Medium");
            tvStrength.setTextColor(Color.rgb(255, 165, 0)); // Orange color
        } else {
            tvStrength.setText("Strong");
            tvStrength.setTextColor(getResources().getColor(R.color.green_color));
        }
    }

    private int getPasswordStrength(String password) {
        boolean hasLowercase = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile("[@#$%^&+=!]").matcher(password).find();
        int length = password.length();

        if (length >= 8 && hasLowercase && hasUppercase && hasDigit && hasSpecialChar) {
            return 3; // Strong
        } else if (length >= 6 && ((hasLowercase && hasUppercase) || (hasDigit && hasSpecialChar))) {
            return 2; // Medium
        } else {
            return 1; // Weak
        }
    }
}
