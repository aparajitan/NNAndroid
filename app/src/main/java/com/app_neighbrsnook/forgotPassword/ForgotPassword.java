package com.app_neighbrsnook.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.utils.UtilityFunction;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPassword extends AppCompatActivity {
    ImageView img_back;
    EditText et_password,et_confirm_password;
    FrameLayout frm_reset_password;
    Context context;
    Activity activity;
    String value;
    ImageView imgShowPassword,imgHidePassword, img_confirm_password,imgHidePasswordConfirm;
    private TextView tvStrength;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile   ("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context=activity=this;
        img_back=findViewById(R.id.img_back);
        imgShowPassword = findViewById(R.id.show_password);
        imgHidePassword = findViewById(R.id.imgHidePassword);
        img_confirm_password = findViewById(R.id.img_confirm_password);
        imgHidePasswordConfirm = findViewById(R.id.imgHidePasswordConfirm);
        et_password=findViewById(R.id.password_reset);
        et_confirm_password=findViewById(R.id.confirm_password_reset);
        frm_reset_password=findViewById(R.id.reset_password);
        tvStrength = findViewById(R.id.tvStrength);
        Bundle extras = getIntent().getExtras();
        value = extras.getString("reqestmobileno");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkPasswordStrength(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        frm_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_password.getText().toString().isEmpty()) {
                    et_password.requestFocus();
                    Toast.makeText(getApplicationContext(), "Password must be minimum 5 digits.", Toast.LENGTH_SHORT).show();
                } else if (et_confirm_password.getText().toString().isEmpty()) {
                    et_confirm_password.requestFocus();
                    et_confirm_password.setError("Please confirm password");
                }
                else if (!et_confirm_password.getText().toString().equals(et_password.getText().toString())){
                    et_confirm_password.requestFocus();
                    et_confirm_password.setError("The passwords don't match");
                }
                else {
                    if (validatePassword()) {

                            forgotPassword();

                    }

                }


            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgShowPassword.setVisibility(View.VISIBLE);  // Show the "Show Password" icon
        imgHidePassword.setVisibility(View.GONE);     // Hide the "Hide Password" icon

// Show password when imgShowPassword is clicked
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgShowPassword.setVisibility(View.GONE);    // Hide the "Show Password" icon
                imgHidePassword.setVisibility(View.VISIBLE); // Show the "Hide Password" icon
                et_password.setSelection(et_password.getText().length()); // Move cursor to end
            }
        });
        imgHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide password
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgHidePassword.setVisibility(View.GONE);    // Hide the "Hide Password" icon
                imgShowPassword.setVisibility(View.VISIBLE); // Show the "Show Password" icon
                et_password.setSelection(et_password.getText().length()); // Move cursor to end
            }
        });

        // Set the password to hidden initially and set the initial icon visibility
        et_confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        img_confirm_password.setVisibility(View.VISIBLE);  // Show the "Show Password" icon
        imgHidePasswordConfirm.setVisibility(View.GONE);     // Hide the "Hide Password" icon

// Show password when imgShowPassword is clicked
        img_confirm_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password
                et_confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                img_confirm_password.setVisibility(View.GONE);    // Hide the "Show Password" icon
                imgHidePasswordConfirm.setVisibility(View.VISIBLE); // Show the "Hide Password" icon
                et_confirm_password.setSelection(et_confirm_password.getText().length()); // Move cursor to end
            }
        });
        imgHidePasswordConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide password
                et_confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgHidePasswordConfirm.setVisibility(View.GONE);    // Hide the "Hide Password" icon
                img_confirm_password.setVisibility(View.VISIBLE); // Show the "Show Password" icon
                et_confirm_password.setSelection(et_confirm_password.getText().length()); // Move cursor to end
            }
        });




    }

    private void forgotPassword(){
        if (UtilityFunction.isNetworkConnected(context)) {
            UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String,String> hashMap=new HashMap<>();
           // hashMap.put("userid", PrefMananger.GetLoginData(context).getId()+"");
            //    hashMap.put("flag","25");

            hashMap.put("password", et_password.getText().toString());//var
            hashMap.put("confirm_password", et_confirm_password.getText().toString());//var
            hashMap.put("phoneno", value);//var
            hashMap.put("status","success");
            ApiExecutor.getApiService().updateApiPassword("forgotpassword",hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, retrofit2.Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getStatus().equals("success")){
                            Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
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
    private boolean validatePassword() {
        String passwordInput = et_password.getText().toString().trim();
        // if password field is empty
        // it will display error message "Field can not be empty"
        if (passwordInput.isEmpty()) {
            et_password.requestFocus();
            et_password.setError("Field can't be empty");
            return false;
        }
        // if password does not matches to the pattern
        // it will display an error message "Password is too weak"
        else if (passwordInput.length() < 5) {
            et_password.requestFocus();
            Toast.makeText(getApplicationContext(), "Password must be minimum 5 digits.", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            et_password.setError(null);
            return true;
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
            tvStrength.setTextColor(R.color.green_color);
        }
    }
    private int getPasswordStrength(String password) {
        // Regex conditions
        boolean hasLowercase = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile("[@#$%^&+=!]").matcher(password).find();

        // Password length
        int length = password.length();

        // **Step-by-step strength checking in sequence**
        if (length >= 8 && hasLowercase && hasUppercase && hasDigit && hasSpecialChar) {
            return 3; // Strong
        } else if (length >= 6 && ((hasLowercase && hasUppercase) || (hasDigit && hasSpecialChar))) {
            return 2; // Medium
        } else {
            return 1; // Weak
        }
    }

}