package com.app_neighbrsnook.forgotPassword;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.OtpVerifyCheckPojo;
import com.app_neighbrsnook.pojo.RegisterationOtpPojo;
import com.app_neighbrsnook.registration.SmsBroadcastReceiver;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordPhoneNo extends AppCompatActivity {
    FrameLayout mbtnVerifyOtp;
    ImageView img_back;
    TextView tv_phone_mail,tv_sent_otp;
    private String verificationId;
    EditText et_one, et_two, et_three, et_four,et_five,et_six;
    Context context;
    Activity activity;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    SharedPrefsManager sm;
    TextInputEditText etOtpDemo;
    SmsBroadcastReceiver smsBroadcastReceiver;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=activity=this;
        setContentView(R.layout.activity_forgot_password_phone_no);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        tv_resend_code = findViewById(R.id.tv_resend_code);
        etOtpDemo=findViewById(R.id.etOtpDemo);

        tv_OtpTimer = findViewById(R.id.tv_OtpTimer);

        mbtnVerifyOtp =findViewById(R.id.forgot_password_id_send);
        img_back=findViewById(R.id.img_back);
        tv_phone_mail=findViewById(R.id.phone_no_id);
        tv_sent_otp=findViewById(R.id.sent_otp);
        et_one = findViewById(R.id.et_one);
        et_two = findViewById(R.id.et_two);
        et_three = findViewById(R.id.et_three);
        et_four = findViewById(R.id.et_four);
        et_five=findViewById(R.id.et_five);
        et_six=findViewById(R.id.et_six);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mbtnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_phone_mail.getText().toString().equals(""))
                {
                    Toast.makeText(ForgotPasswordPhoneNo.this, "Mobile number is empty", Toast.LENGTH_SHORT).show();
                }else if (tv_phone_mail.getText().toString().length()<10)
                {
                    Toast.makeText(ForgotPasswordPhoneNo.this, "Mobile number is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (tv_phone_mail.getText().toString().length()>=11){
                    Toast.makeText(ForgotPasswordPhoneNo.this, "Please 10 digit mobile", Toast.LENGTH_SHORT).show();

                }else {
                    checkVerify();
                //    otpSend(tv_phone_mail.getText().toString());
                }

            }


        });
        et_one.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_one.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_two.requestFocus();
                }
                OTP = GetOTP();
                if (OTP.length() == 6) {
                    //  frm_register.setVisibility(View.VISIBLE);
                } else {
                    //frm_register.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        et_two.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_two.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_three.requestFocus();
                } else if (et_two.getText().toString().length() == 0) {
                    et_one.requestFocus();
                }
                OTP = GetOTP();
                if (OTP.length() == 6) {
                    // frm_register.setVisibility(View.VISIBLE);
                } else {
                    //frm_register.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        et_three.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_three.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_four.requestFocus();
                } else if (et_three.getText().toString().length() == 0) {
                    et_two.requestFocus();
                }
                OTP = GetOTP();
                if (OTP.length() == 6) {
                    // frm_register.setVisibility(View.VISIBLE);
                } else {
                    //frm_register.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void afterTextChanged(Editable s) {

            }
        });
        et_four.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_four.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_five.requestFocus();
                } else if (et_four.getText().toString().length() == 0) {
                    et_three.requestFocus();
                }
                OTP = GetOTP();
                if (OTP.length() == 6) {
                    // frm_register.setVisibility(View.VISIBLE);
                } else {
                    //frm_register.setVisibility(View.GONE);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });
        et_five.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_five.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_six.requestFocus();
                } else if (et_five.getText().toString().length() == 0) {
                    et_four.requestFocus();
                }
                OTP = GetOTP();
                if (OTP.length() == 6) {
                    // frm_register.setVisibility(View.VISIBLE);
                } else {
                    //frm_register.setVisibility(View.GONE);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });
        et_six.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_six.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_six.requestFocus();
                      otp = et_one.getText().toString()+et_two.getText().toString()+et_three.getText().toString()+et_four.getText().toString()+et_five.getText().toString()+et_six.getText().toString();
                    //verifyCode(otp);
                } else if (et_six.getText().toString().length() == 0) {
                    et_five.requestFocus();
                }
                OTP = GetOTP();
                if (OTP.length() == 6) {


                    // frm_register.setVisibility(View.VISIBLE);
                } else {
                    //frm_register.setVisibility(View.GONE);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });
        et_one.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    et_one.setText("");
                    UtilityFunction.hideKeybord(context, v);
                    return true;
                }
                return false;
            }
        });

        et_two.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    et_two.setText("");
                    et_one.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_three.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    et_three.setText("");
                    et_two.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_four.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    et_four.setText("");
                    et_three.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_five.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    et_five.setText("");
                    et_four.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_six.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    et_six.setText("");
                    et_five.requestFocus();
                    return true;
                }
                return false;
            }
        });

        tv_sent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_phone_mail.getText().toString().equals(""))
                {
                    Toast.makeText(ForgotPasswordPhoneNo.this, "Mobile number is empty", Toast.LENGTH_SHORT).show();
                }else if (tv_phone_mail.getText().toString().length()<10)
                {
                    Toast.makeText(ForgotPasswordPhoneNo.this, "Mobile number is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (tv_phone_mail.getText().toString().length()>=11){
                    Toast.makeText(ForgotPasswordPhoneNo.this, "Please 10 digit mobile", Toast.LENGTH_SHORT).show();

                }else {
//                    otp();
                    otpSend(tv_phone_mail.getText().toString());
                }

            }
        });

        tv_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_phone_mail.getText().toString().isEmpty()) {
                    tv_phone_mail.requestFocus();
                    tv_phone_mail.setError("Please enter mobile no");
                } else {

                    //otp();
                    otpSend(tv_phone_mail.getText().toString());

                }
            }
        });
        startSmartUserConsent();

    }

    private void startSmartUserConsent() {
        SmsRetrieverClient client= SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        String message=data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                        getOtpFromMessage(message);
                    }
                }
            });

    private void getOtpFromMessage(String  message) {
        Pattern otpPattern=Pattern.compile("(|^)\\d{6}");
        Matcher matcher=otpPattern.matcher(message);
        if (matcher.find()){
            etOtpDemo.setText(matcher.group(0));
            String fullMsg = etOtpDemo.getText().toString();
            char o1 = fullMsg.charAt(0);
            char o2 = fullMsg.charAt(1);
            char o3 = fullMsg.charAt(2);
            char o4 = fullMsg.charAt(3);
            char o5 = fullMsg.charAt(4);
            char o6 = fullMsg.charAt(5);
            et_one.setText(o1 + "");
            et_two.setText(o2 + "");
            et_three.setText(o3 + "");
            et_four.setText(o4 + "");
            et_five.setText(o5 + "");
            et_six.setText(o6 + "");


        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsBroadcastReceiver != null) {
            unregisterReceiver(smsBroadcastReceiver);
        }
    }

    private TextView tv_resend_code, tv_OtpTimer;
    private CountDownTimer countDownTimer;
    private void startCountDown() {
        tv_OtpTimer.setVisibility(View.VISIBLE);
        tv_resend_code.setVisibility(View.GONE);
        tv_sent_otp.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                //tv_resend_code.setVisibility(View.VISIBLE);
                //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                tv_OtpTimer.setText("" + String.format("%d:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tv_resend_code.setVisibility(View.VISIBLE);
                tv_OtpTimer.setVisibility(View.GONE);
            }
        }.start();
    }

    String OTP;
    String otp = "";

    public String GetOTP() {
        String GETOTP = "";
        String Otp1 = et_one.getText().toString();
        String Otp2 = et_two.getText().toString();
        String Otp3 = et_three.getText().toString();
        String Otp4 = et_four.getText().toString();

        String Otp5 = et_five.getText().toString();
        String Otp6 = et_six.getText().toString();

        return GETOTP;
    }

    public void otpSend(String phoneNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        APIInterface apiService = APIClient.getRetrofit().create(APIInterface.class);
        Call<RegisterationOtpPojo> call = apiService.forgtoOtp(phoneNumber);
        call.enqueue(new Callback<RegisterationOtpPojo>() {
            @Override
            public void onResponse(Call<RegisterationOtpPojo> call, retrofit2.Response<RegisterationOtpPojo> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    RegisterationOtpPojo otpResponse = response.body();
                    if ("success".equalsIgnoreCase(otpResponse.getStatus())) {
                        startCountDown();
                        resetOtpFields();
                        Toast.makeText(context, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch response!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterationOtpPojo> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("OTP Error", t.toString());
                Toast.makeText(context, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetOtpFields() {
        EditText[] otpFields = {et_one, et_two, et_three, et_four, et_five, et_six};
        for (EditText field : otpFields) {
            field.setText("");
        }
        et_one.requestFocus(); // Set focus to first field
    }
    private void checkVerify() {
        if (UtilityFunction.isNetworkConnected(context)) {
            UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("reqestmobileno", tv_phone_mail.getText().toString());
            hashMap.put("otpvarify", otp);
         //   Log.d("otper",otp);
            hashMap.put("status", "success");
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<OtpVerifyCheckPojo> call = service.checkOtp("verification",hashMap);
            call.enqueue(new Callback<OtpVerifyCheckPojo>() {
                @Override
                public void onResponse(Call<OtpVerifyCheckPojo> call, retrofit2.Response<OtpVerifyCheckPojo> response) {
                    String status = response.body().getStatus();
                    UtilityFunction.hideLoading();
                    try {
                        if (status.equals("success")) {
                            JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body().getDescription()));
                            String desc =jsonObject.getString("desc");
                            if (desc.equals ("Code Matched successfully.")){
                               // imgRightIcon.setVisibility(View.VISIBLE );
                                Toast.makeText(ForgotPasswordPhoneNo.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                                Intent memberIntent = new Intent(ForgotPasswordPhoneNo.this, ForgotPassword.class);
                                memberIntent.putExtra("reqestmobileno",tv_phone_mail.getText().toString());
                                startActivity(memberIntent);

                            }
                            else if (desc.equals("Code does not match.")){
                                Toast.makeText(ForgotPasswordPhoneNo.this, "Incorrect otp", Toast.LENGTH_SHORT).show();
                               // imgRightIcon.setVisibility(View.GONE );

                            }
                        }
                    }catch (Exception e){

                    }

                }

                @Override
                public void onFailure(Call<OtpVerifyCheckPojo> call, Throwable t) {
                    Toast.makeText(ForgotPasswordPhoneNo.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    UtilityFunction.hideLoading();
                    Log.d("restrrr", t.getMessage());
                }
            });
        }

    }
    }


