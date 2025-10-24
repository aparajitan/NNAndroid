package com.app_neighbrsnook.registration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EmailPojoStatus;
import com.app_neighbrsnook.pojo.LoginPojo;
import com.app_neighbrsnook.pojo.OtpVerifyCheckPojo;
import com.app_neighbrsnook.pojo.RegisterationOtpPojo;
import com.app_neighbrsnook.termsCondition.PrivacyPolicy;
import com.app_neighbrsnook.termsCondition.TermsConditions;
import com.app_neighbrsnook.utils.AppSignatureHelper;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.MetaEventLogger;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.Application;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
public class FirstPageRegisteration extends AppCompatActivity implements SmsBroadcastReceiver.SmsBroadcastReceiverListener {
    TextView tv_sign;
    FrameLayout frm_register, frm_privacy_policy;
    EditText tv_first_name, tv_last_name, tv_phone_no, et_confirm_password;
    EditText et_one, et_two, et_three, et_four, et_five, et_six;
    Activity activity;
    Context context;
    TextView tv_send_otp, checkeMail;
    ImageView imgShowPassword, imgHidePassword, img_confirm_password, imgRightIcon, imgCrossIcon;
    private EditText et_password;
    private EditText tv_mail;
    TextView tv_terms_condition;
    SmsBroadcastReceiver smsBroadcastReceiver;
    TextView tv_you_have;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    CheckBox checkBox;
    Dialog dialog;
    TextView ok;
    SharedPrefsManager sm;
    TextInputEditText etOtpDemo;
    boolean isVerifiedOtp = false;
    boolean isEmailVerified = true;
    private FirebaseAnalytics mFirebaseAnalytics;
    String token;
    private Runnable emailCheckRunnable;
    private TextView tvStrength;
    String smUserId;
    FrameLayout frmOtpLayout;
    FusedLocationProviderClient mFusedLocationClient;
    Bitmap bitmap1;
    CircleImageView img_profile;
    String OTP;
    String otp = "";
    private TextView tv_resend_code, tv_OtpTimer;
    private CountDownTimer countDownTimer;
    private static final int SMS_CONSENT_REQUEST = 1234;
    private boolean isAutoFilling = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_registration_demo);

        SharedPreferences prefs = getSharedPreferences("AddressProofPrefs", MODE_PRIVATE);
        prefs.edit().remove("manualSelected").apply();

        sm = new SharedPrefsManager(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize SMS receiver
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener = this;

        // Register receiver with proper flags for Android 14+
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(smsBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(smsBroadcastReceiver, intentFilter);
        }
        item();
        MetaEventLogger.testMetaEvents(FirstPageRegisteration.this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "manual_debug");
        FirebaseAnalytics.getInstance(this).logEvent("test_event", bundle);

        if (!GlobalMethods.hasAllRequiredPermissions(this)) {
            GlobalMethods.requestNotificationPermission(this);
        }

        bitmap1 = GlobalMethods.getInstance(context).getInitialBitmap(img_profile, sm.getString("user_name"), FirstPageRegisteration.this);
        img_profile.setImageBitmap(bitmap1);

        tv_you_have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstPageRegisteration.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        tv_terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstPageRegisteration.this, TermsConditions.class));
            }
        });
        frm_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstPageRegisteration.this, PrivacyPolicy.class));
            }
        });

        startSmartUserConsent();
        setupSmsReceiver();
        logAppSignatures();

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
                if (et_six.getText().toString().length() == 1) {
                    et_six.requestFocus();
                    tv_mail.requestFocus();
                    if (!isAutoFilling) { // Only proceed if not autofilling
                        otp = et_one.getText().toString() + et_two.getText().toString() +
                                et_three.getText().toString() + et_four.getText().toString()
                                + et_five.getText().toString() + et_six.getText().toString();
                        checkVerify1();
                    }
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

        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                checkPasswordStrength(password);

                if (validatePassword()) {
                    et_password.setError(null);
                }

                String confirmPassword = et_confirm_password.getText().toString();
                if (!confirmPassword.isEmpty() && confirmPassword.equals(password)) {
                    et_confirm_password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        et_password.addTextChangedListener(passwordTextWatcher);
        tv_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstPageRegisteration.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        imgShowPassword.setVisibility(View.VISIBLE);
        imgHidePassword.setVisibility(View.GONE);
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        tv_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_phone_no.getText().toString().equals("")) {
                    tv_phone_no.requestFocus();
                    tv_phone_no.setError("Please enter mobile no");
                } else if (tv_phone_no.getText().toString().length() < 10) {
                    tv_phone_no.requestFocus();
                    tv_phone_no.setError("Mobile number is invalid");
                } else {
                    otp(tv_phone_no.getText().toString());
                }
                frmOtpLayout.setVisibility(View.VISIBLE);
            }
        });
        tv_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_phone_no.getText().toString().isEmpty()) {
                    tv_phone_no.requestFocus();
                    tv_phone_no.setError("Please enter mobile no");
                } else {
                    otp(tv_phone_no.getText().toString());
                }
            }
        });
        frm_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = tv_first_name.getText().toString().trim();
                String email = tv_mail.getText().toString().trim();
                String phone = tv_phone_no.getText().toString().trim();
                String password = et_password.getText().toString();
                otp = et_one.getText().toString() + et_two.getText().toString() +
                        et_three.getText().toString() + et_four.getText().toString() +
                        et_five.getText().toString() + et_six.getText().toString();
                if (firstName.isEmpty()) {
                    tv_first_name.requestFocus();
                    tv_first_name.setError("Please enter full name");
                    return;
                }
                if (BadWordFilter.containsBadWord(firstName)) {
                    tv_first_name.requestFocus();
                    tv_first_name.setError("Please use your name as it appears on your government ID.");
                    return;
                }
                if (phone.isEmpty()) {
                    tv_phone_no.requestFocus();
                    tv_phone_no.setError("Please enter mobile no");
                    return;
                }
                if (otp.isEmpty()) {
                    et_one.requestFocus();
                    Toast.makeText(context, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (otp.length() < 6) {
                    Toast.makeText(context, "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (otp.length() < 3) {
                    otp = GetOTP();
                }
                if (email.isEmpty()) {
                    tv_mail.requestFocus();
                    tv_mail.setError("Please enter e-mail");
                    return;
                }
                if (!validateEmail()) {
                    tv_mail.requestFocus();
                    tv_mail.setError("Please enter a valid email address");
                    return;
                }
                if (password.isEmpty() || !validatePassword()) {
                    et_password.requestFocus();
                    et_password.setError("Password must be minimum 5 digits");
                    return;
                }
                // Final checks
               /* if (!isVerifiedOtp) {
                    Toast.makeText(activity, "Please verify your OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isEmailVerified) {
                    Toast.makeText(activity, "Please use a valid email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkBox.isChecked()) {
                    globalDialog(); // Show terms & conditions dialog
                    return;
                }*/
                signup();
           /*     emailChecked(new EmailVerificationCallback() {
                    @Override
                    public void onResult(boolean isVerified, String message) {
                        if (isVerified) {
                            signup(); // ✅ Email verify hai, signup call karo
                        } else {
                            //Toast.makeText(RegistrationActivity.this, "Please use a valid email", Toast.LENGTH_SHORT).show();
                            tv_mail.setError("Please use a valid email");
                            tv_mail.requestFocus();
                        }
                    }
                });*/
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("FCM", "Fetching FCM token failed", task.getException());
                    return;
                }
                token = task.getResult();
                Log.d("FCM Token", token);
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("general");
    }

    private void item() {
        imgShowPassword = findViewById(R.id.show_password);
        tv_sign = findViewById(R.id.tv_sign_in);
        imgHidePassword = findViewById(R.id.imgHidePassword);
        ok = findViewById(R.id.ok);
        frm_register = findViewById(R.id.registration_id);
        tv_first_name = findViewById(R.id.first_name_id_tv);
        tv_last_name = findViewById(R.id.last_name_id);
        tv_mail = findViewById(R.id.email_id);
        tv_phone_no = findViewById(R.id.phone_no_id);
        et_password = findViewById(R.id.password_id);
        tvStrength = findViewById(R.id.tvStrength);
        et_confirm_password = findViewById(R.id.confirm_password_id);
        et_one = findViewById(R.id.et_one);
        et_two = findViewById(R.id.et_two);
        et_three = findViewById(R.id.et_three);
        et_four = findViewById(R.id.et_four);
        et_five = findViewById(R.id.et_five);
        et_six = findViewById(R.id.et_six);
        etOtpDemo = findViewById(R.id.etOtpDemo);
        tv_resend_code = findViewById(R.id.tv_resend_code);
        imgRightIcon = findViewById(R.id.imgRightIcon);
        imgCrossIcon = findViewById(R.id.imgCrossIcon);
        tv_OtpTimer = findViewById(R.id.tv_OtpTimer);
        tv_send_otp = findViewById(R.id.sent_otp);
        img_profile = findViewById(R.id.cicle_image_view_id);
        tv_terms_condition = findViewById(R.id.terms_and_condition);
        checkeMail = findViewById(R.id.checkeMail);
        tv_you_have = findViewById(R.id.you_have_accound);
        checkBox = findViewById(R.id.checkbox_register);
        img_confirm_password = findViewById(R.id.img_confirm_password);
        frm_privacy_policy = findViewById(R.id.privacy_policy);
        frmOtpLayout = findViewById(R.id.frmOtpLayout);
    }

    private void logAppSignatures() {
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        List<String> appSignatures = appSignatureHelper.getAppSignatures();

        Log.d("APP_SIGNATURE", "App Signatures for SMS Retriever:");
        for (String signature : appSignatures) {
            Log.d("APP_SIGNATURE", "Hash: " + signature);
            // You can also display it in a Toast or TextView for easy testing
        }

        // Display the first signature in a Toast for easy testing
        if (!appSignatures.isEmpty()) {
//            Toast.makeText(this, "App Hash: " + appSignatures.get(0), Toast.LENGTH_LONG).show();
        }
    }

// Update your SMS receiver initialization
    private void setupSmsReceiver() {
        // Unregister previous receiver if exists
        if (smsBroadcastReceiver != null) {
            try {
                unregisterReceiver(smsBroadcastReceiver);
            } catch (Exception e) {
                Log.e("SMS Receiver", "Error unregistering receiver", e);
            }
        }

        // Create new receiver
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener = this;

        // Register receiver
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(smsBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(smsBroadcastReceiver, intentFilter);
        }
    }

    private void startSmartUserConsent() {
        try {
            SmsRetrieverClient client = SmsRetriever.getClient(this);
            Task<Void> task = client.startSmsUserConsent(null);
            task.addOnSuccessListener(aVoid -> {
                Log.d("SMS", "SMS Retriever started successfully for OTP: " + tv_phone_no.getText().toString());
            });
            task.addOnFailureListener(e -> {
                Log.e("SMS", "Failed to start SMS Retriever", e);
                // Optional: Retry after delay
            });
        } catch (Exception e) {
            Log.e("SMS", "Exception starting SMS Retriever", e);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                isAutoFilling = true; // Set flag before autofill
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                if (message != null) {
                    String otp = extractOtpFromMessage(message);
                    if (otp != null && otp.length() == 6) {
                        et_one.setText(String.valueOf(otp.charAt(0)));
                        et_two.setText(String.valueOf(otp.charAt(1)));
                        et_three.setText(String.valueOf(otp.charAt(2)));
                        et_four.setText(String.valueOf(otp.charAt(3)));
                        et_five.setText(String.valueOf(otp.charAt(4)));
                        et_six.setText(String.valueOf(otp.charAt(5)));

                        this.otp = otp;
                        checkVerify1();
                    }
                }
                isAutoFilling = false; // Reset flag after autofill
            }
        }
    }

    private String extractOtpFromMessage(String message) {
        Pattern pattern = Pattern.compile("is\\s+(\\d{6})");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Implement the SmsBroadcastReceiverListener methods
    @Override
    public void onSuccess(Intent intent) {
        // Validate the intent before starting the activity
        if (intent != null && intent.getPackage() != null && intent.getPackage().equals("com.google.android.gms")) {
            try {
                startActivityForResult(intent, SMS_CONSENT_REQUEST);
            } catch (Exception e) {
                Log.e("SMS Consent", "Failed to start consent activity", e);
                onFailure(); // Call the failure method directly
            }
        } else {
            // Handle the case where the intent is not from a trusted source
            Log.e("SMS Consent", "Received intent from untrusted source: " + (intent != null ? intent.getPackage() : "null"));
            onFailure(); // Call the failure method directly
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Failed to retrieve SMS", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsBroadcastReceiver != null) {
            unregisterReceiver(smsBroadcastReceiver);
        }
    }

    public void signup() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        logRegistrationEvent("first_step_started_android_app", "step_one_done");

        // 1. Name uthao
        String firstName = tv_first_name.getText().toString().trim();

        // 2. Bitmap banao che(initials se)
        Bitmap generatedBitmap = GlobalMethods.getInstance(context)
                .getInitialBitmap(new ImageView(context), firstName, this);

        // 3. Bitmap ko Multipart mein convert karo
        MultipartBody.Part userpic = null;
        if (generatedBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            generatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), byteArray);
            userpic = MultipartBody.Part.createFormData("userpic", "profile.png", requestFile);
        }

        // 4. Baaki fields ko RequestBody banao
        RequestBody firstname = RequestBody.create(MultipartBody.FORM, firstName);
        RequestBody email = RequestBody.create(MultipartBody.FORM, tv_mail.getText().toString());
        RequestBody phone = RequestBody.create(MultipartBody.FORM, tv_phone_no.getText().toString());
        RequestBody password = RequestBody.create(MultipartBody.FORM, et_password.getText().toString());
        RequestBody term = RequestBody.create(MultipartBody.FORM, "1");
        RequestBody tokenBody = RequestBody.create(MultipartBody.FORM, token);

        // 5. API Call
        APIInterface apiService = APIClient.getRetrofit().create(APIInterface.class);
        Call<ResponseBody> call = apiService.signupDemo(firstname, email, phone, password, term, tokenBody, userpic);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        String status = jsonObject.getString("status");

                        if (status.equals("success") && jsonObject.has("userid")) {
                            String userid = jsonObject.getString("userid");
                            int referralStatus = 0;
                            if (jsonObject.has("referral_status")) {
                                referralStatus = jsonObject.getInt("referral_status");
                            }

                            // ✅ Save referral_status in SharedPreferences
                            sm.setInt("referral_status", referralStatus);

                            // ✅ Save basic user info in LoginPojo + SharedPreferences
                            LoginPojo loginPojo = new LoginPojo();
                            loginPojo.setUsername(firstName);
                            sm.setString("user_name", firstName);
                            sm.setString("phone_no", tv_phone_no.getText().toString());
                            loginPojo.setEmailid(tv_mail.getText().toString());
                            loginPojo.setPhoneno(tv_phone_no.getText().toString());
                            loginPojo.setId(userid);
                            sm.setString("user_id", userid);
                            smUserId = userid;
                            PrefMananger.SaveLoginData(context, loginPojo);

                            // ✅ Referral data save only if referral_status == 1
                            if (referralStatus == 1) {
                                if (jsonObject.has("refer_neighbourhood_id")) {
                                    String id = jsonObject.getString("refer_neighbourhood_id");
                                    sm.setString("refer_neighbourhood_id", id);
                                    Log.d("SignupResponseData", "Neighbourhood ID: " + id);
                                }

                                if (jsonObject.has("refer_neighbourhood_name")) {
                                    String name = jsonObject.getString("refer_neighbourhood_name");
                                    sm.setString("refer_neighbourhood_name", name);
                                    Log.d("SignupResponseData", "Neighbourhood Name: " + name);
                                }

                                if (jsonObject.has("refer_city_name")) {
                                    String city = jsonObject.getString("refer_city_name");
                                    sm.setString("refer_city_name", city);
                                    Log.d("SignupResponseData", "City Name: " + city);
                                }

                                if (jsonObject.has("refer_state_name")) {
                                    String state = jsonObject.getString("refer_state_name");
                                    sm.setString("refer_state_name", state);
                                    Log.d("SignupResponseData", "State Name: " + state);
                                }

                                if (jsonObject.has("refer_country_name")) {
                                    String country = jsonObject.getString("refer_country_name");
                                    sm.setString("refer_country_name", country);
                                    Log.d("SignupResponseData", "Country Name: " + country);
                                }

                                if (jsonObject.has("refer_pincode")) {
                                    String pincode = jsonObject.getString("refer_pincode");
                                    sm.setString("refer_pincode", pincode);
                                    Log.d("SignupResponseData", "Pincode: " + pincode);
                                }
                            } else {
                                // 🧹 Clear old referral data (if any)
                                sm.setString("refer_neighbourhood_id", "");
                                sm.setString("refer_neighbourhood_name", "");
                                sm.setString("refer_city_name", "");
                                sm.setString("refer_state_name", "");
                                sm.setString("refer_country_name", "");
                                sm.setString("refer_pincode", "");
                            }

                            // ✅ Move to next screen with proper source
                            Intent intent = new Intent(FirstPageRegisteration.this, SecondPageUserLocationRegisteration.class);
                            intent.putExtra("step2_page_type", "new");

                            if (referralStatus == 1) {
                                intent.putExtra("source", "referral"); // Referral user
                            } else {
                                intent.putExtra("source", "register"); // Normal user
                            }

                            startActivity(intent);
                            finishAffinity();

                        } else {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "An error occurred.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Retrofit Error", t.getMessage());
                Toast.makeText(context, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateEmail() {
        String emailInput = tv_mail.getText().toString().trim();
        Log.d("Validation", "validateEmail called"); // Check if this logs
        if (emailInput.isEmpty()) {
            tv_mail.requestFocus();
            tv_mail.setError("Please enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            tv_mail.requestFocus();
            tv_mail.setError("Please enter a valid email address");
            return false;
        } else {
            tv_mail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = et_password.getText().toString();
        return password.length() >= 5;
    }

    private void startCountDown() {
        tv_OtpTimer.setVisibility(View.VISIBLE);
        tv_resend_code.setVisibility(View.GONE);
        tv_send_otp.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
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

    public void otp(String phoneNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        APIInterface apiService = APIClient.getRetrofit().create(APIInterface.class);
        Call<RegisterationOtpPojo> call = apiService.sendOtp(phoneNumber);
        call.enqueue(new Callback<RegisterationOtpPojo>() {
            @Override
            public void onResponse(Call<RegisterationOtpPojo> call, Response<RegisterationOtpPojo> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    RegisterationOtpPojo otpResponse = response.body();
                    if ("success".equalsIgnoreCase(otpResponse.getStatus())) {
                        startCountDown();
                        resetOtpFields();
                        startSmartUserConsent(); // Restart SMS Retriever for new OTP
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

    private void checkVerify1() {
        if (UtilityFunction.isNetworkConnected(context)) {
            UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("reqestmobileno", tv_phone_no.getText().toString());
            hashMap.put("otpvarify", otp);
            hashMap.put("status", "success");
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<OtpVerifyCheckPojo> call = service.checkOtp("verification", hashMap);
            call.enqueue(new Callback<OtpVerifyCheckPojo>() {
                @Override
                public void onResponse(Call<OtpVerifyCheckPojo> call, Response<OtpVerifyCheckPojo> response) {
                    UtilityFunction.hideLoading();

                    if (response.body() == null || response.body().getStatus() == null) {
                        //Log.e("OTPVerify", "Response body or status is null");
                        Toast.makeText(FirstPageRegisteration.this, "Verification failed. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String status = response.body().getStatus();
                    if ("success".equals(status)) {
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body().getDescription()));
                            String desc = jsonObject.getString("desc");

                            if ("Code Matched successfully.".equals(desc)) {
                                imgRightIcon.setVisibility(View.VISIBLE);
                                imgCrossIcon.setVisibility(View.GONE);

                                isVerifiedOtp = true;
                                Toast.makeText(FirstPageRegisteration.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                            } else if ("Code does not match.".equals(desc)) {
                                isVerifiedOtp = false;
                                Toast.makeText(FirstPageRegisteration.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                                imgCrossIcon.setVisibility(View.VISIBLE);
                                imgRightIcon.setVisibility(View.GONE);

                            }
                        } catch (Exception e) {
                            Log.e("OTPVerify", "Error parsing response: " + e.getMessage(), e);
                            Toast.makeText(FirstPageRegisteration.this, "Error processing verification. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        isVerifiedOtp = false;
                        Log.e("OTPVerify", "Verification failed with status: " + status);
                        Toast.makeText(FirstPageRegisteration.this, "Verification failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OtpVerifyCheckPojo> call, Throwable t) {
                    UtilityFunction.hideLoading();
                    Log.e("OTPVerify", "API call failed: " + t.getMessage(), t);
                    Toast.makeText(FirstPageRegisteration.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(FirstPageRegisteration.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void globalDialog() {
        TextView msg, ok;
        dialog = new Dialog(FirstPageRegisteration.this);
        dialog.setContentView(R.layout.terms_condition_layout);
        ok = dialog.findViewById(R.id.ok_textview);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(FirstPageRegisteration.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        msg = dialog.findViewById(R.id.msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void logRegistrationEvent(String eventName, String status) {
        Bundle bundle = new Bundle();
        bundle.putString("status", status);  // Success, Failed, or Started
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    private void emailChecked(EmailVerificationCallback callback) {
        String email = tv_mail.getText().toString().trim();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", email);
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<EmailPojoStatus> call = service.emailVerify(hashMap);

        call.enqueue(new Callback<EmailPojoStatus>() {
            @Override
            public void onResponse(Call<EmailPojoStatus> call, Response<EmailPojoStatus> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EmailPojoStatus statusPojo = response.body();
                    boolean isVerified = statusPojo.getStatus();
                    callback.onResult(isVerified, statusPojo.getMessage());
                } else {
                    callback.onResult(false, "Server error, please try again!");
                }
            }

            @Override
            public void onFailure(Call<EmailPojoStatus> call, Throwable t) {
                callback.onResult(false, "Request failed: " + t.getMessage());
            }
        });
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
            tvStrength.setTextColor(Color.rgb(0, 128, 0)); // Green color
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

    public interface EmailVerificationCallback {
        void onResult(boolean isVerified, String message);
    }
}