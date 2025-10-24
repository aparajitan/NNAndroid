package com.app_neighbrsnook.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.app_neighbrsnook.BaseActivity;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.forgotPassword.ForgotPasswordPhoneNo;
import com.app_neighbrsnook.registration.SecondPageUserLocationRegisteration;
import com.app_neighbrsnook.registration.FirstPageRegisteration;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.model.AppLimitPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.LoginPojo;
import com.app_neighbrsnook.utils.ColorUtils;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivity extends BaseActivity {
    LinearLayout tv_registration_activity;
    TextView  signin, tv_forgot, tv_dont_have;
    EditText et_email_phone, et_password;
    FrameLayout frm_sign_in, frm_email, frm_password;
    SharedPrefsManager sm;
    ImageView imgShowPassword,imgHidePassword;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Context context;
    Activity activity;
    UrlClass urlClass = new UrlClass();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = activity =this;
        sm = new SharedPrefsManager(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SharedPreferences prefs = getSharedPreferences("AddressProofPrefs", MODE_PRIVATE);
        prefs.edit().remove("manualSelected").apply();

        tv_forgot = findViewById(R.id.tv_forgot_password);
        frm_sign_in = findViewById(R.id.sign_in_id);
        frm_email = findViewById(R.id.frm_email);
        frm_password = findViewById(R.id.frm_password);
        et_email_phone = findViewById(R.id.et_email);
        et_password = findViewById(R.id.password_login_id);
        tv_registration_activity = findViewById(R.id.tv_registeration);
        signin = findViewById(R.id.btn_update_password);
        imgShowPassword = findViewById(R.id.img_show_password_login);
        imgHidePassword = findViewById(R.id.imgHidePassword);
        tv_dont_have = findViewById(R.id.tv_id_dont_have);
      /* et_email_phone.setText("6125177844");
      et_password.setText("Admin");*/
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "manual_test");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP , bundle);
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordPhoneNo.class));
                // startActivity(new Intent(LoginActivity.this, RegistrationActivityDemo.class));
            }
        });
        tv_registration_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FirstPageRegisteration.class);
                startActivity(intent);
            }
        });

        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        imgShowPassword.setVisibility(View.VISIBLE);  // Show the "Show Password" icon
        imgHidePassword.setVisibility(View.GONE);     // Hide the "Hide Password" icon
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
        if (LoginActivity.InternetConnection.checkConnection(context)) {
            appImageSize();
        } else {
            GlobalMethods.getInstance(LoginActivity.this).globalDialog(context, "     No internet connection.     "     );
        }
        frm_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_phone.getText().toString().isEmpty()) {
                    et_email_phone.requestFocus();
                    et_email_phone.setError("Please enter email/mobile no");
                }
                else if (et_password.getText().toString().isEmpty()) {
                    et_password.requestFocus();
                    et_password.setError("Please enter password");
                } else
                {
                    if (InternetConnection.checkConnection(context)) {
                        loginApi();
                    }else{
                        GlobalMethods.getInstance(LoginActivity.this).globalDialog(context, "     No internet connection.     "     );

                    }
                }
            }
        });
        clearAppCache();
    }
    private void appImageSize() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<AppLimitPojo> call = service.appImageSettingApi("userapplimit");
        call.enqueue(new Callback<AppLimitPojo>() {
            @Override
            public void onResponse(Call<AppLimitPojo> call, retrofit2.Response<AppLimitPojo> response) {
                try {
                    if(response.body().getStatus().equals("success"))
                    {
                        sm.setString("business_image_limit", response.body().getBusinessImgLimit());
                        sm.setString("business_doc_limit", response.body().getBusinessDocLimit());
                        sm.setString("post_image_limit", response.body().getPostImageSize());
                        sm.setString("event_img_limit", response.body().getEventImgLimit());
                        sm.setString("event_img_limit", response.body().getEventImgLimit());
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(Call<AppLimitPojo> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("res---", t.getMessage());
            }
        });
    }
    public void loginApi() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiExecutor.getApiService().postRequest(urlClass.getLoginUrl(),getLoginParams()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String status = jsonObject.getString("status");
                        if (status.equals("success") && jsonObject.has("logindata")) {
                            handleLoginSuccess(jsonObject.getJSONObject("logindata"));
                            //    getLoginParams();
                        } else {
                            handleLoginFailure(jsonObject);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.dismiss();
                welcomeDialog("Invalid response format");
            }
        });
    }
  /*  private void handleLoginFailure(JSONObject jsonObject) throws JSONException {
        String message = jsonObject.getString("message");
        switch (message) {
            // sm.setString("user_id",  "id");
            case "DOB Incomplete":
                sm.setString("user_id", jsonObject.getString("id"));
                navigateToActivity(AddressProofLocation.class);//31-07 AddressProof
                break;
            case "Address Incomplete":
                sm.setString("user_id", jsonObject.getString("id")); // Save 'id' using sm
                Log.d("sfsfeweweee",jsonObject.getString("id"));
                navigateToActivity(AddressProofLocation.class);//31-07 AddressProof
                break;
            case "Incomplete documents.":
                navigateToActivity(AddressProofLocation.class);//31-07 AddressProof
                break;
            case "Neighborhood Assigned to user by Admin":
                navigateToActivity(AddressProofLocation.class);
                break;
            case "You can login, Neighbourhood could not be found then take him to address page":
                sm.setString("user_id", jsonObject.getString("id"));
                navigateToActivity(AddressProofLocation.class);
                break;
            default:
                welcomeDialog(message);  // Show message in dialog only if it doesn't match specific cases
        }
    }
    private void handleLoginSuccess(JSONObject logindata) throws JSONException {
        LoginPojo loginPojo = new Gson().fromJson(logindata.toString(), LoginPojo.class);
        PrefMananger.saveLoginData(getApplicationContext(), loginPojo);

        sm.setString("user_id", logindata.getInt("id")+"");
        sm.setString("firebase_token", logindata.getString("firebase_token"));
        sm.setString("user_name", logindata.getString("username"));
        sm.setInt("user_verified", logindata.getInt("verified"));
        Log.e("asdf userId", logindata.getInt("id")+"");
      //
        saveAdditionalUserData(logindata);
        int verified = logindata.getInt("verified");
        int getRequest = logindata.getInt("req_ndbstatus");
        if (verified == 1 || verified == 2 || verified == 0) {
            loginSuccessful(verified);
        } else if (getRequest == 3 || getRequest == 4) {
            navigateToActivity(AddressProofLocation.class);
        }

*//*
        else if ((getRequest == 3 && verified == 0) || getRequest == 4) {
            navigateToActivity(AddressProof.class);
        }
*//*

    }*/



    private void handleLoginFailure(JSONObject jsonObject) throws JSONException {
        String message = jsonObject.getString("message");
        switch (message) {
            case "DOB Incomplete":
                sm.setString("user_id", jsonObject.getString("id"));
                Intent intent1 = new Intent(LoginActivity.this, SecondPageUserLocationRegisteration.class);
                intent1.putExtra("step2_page_type", "new");
                startActivity(intent1);
                finish();
                break;
            case "Address Incomplete":
                sm.setString("user_id", jsonObject.getString("id"));
                Intent intent2 = new Intent(LoginActivity.this, SecondPageUserLocationRegisteration.class);
                intent2.putExtra("step2_page_type", "new");
                startActivity(intent2);
                finish();
                break;
            case "Incomplete documents.":
                Intent intent3 = new Intent(LoginActivity.this, SecondPageUserLocationRegisteration.class);
                intent3.putExtra("step2_page_type", "new");
                startActivity(intent3);
                finish();
                break;
            case "Neighborhood Assigned to user by Admin":
                Intent intent4 = new Intent(LoginActivity.this, SecondPageUserLocationRegisteration.class);
                intent4.putExtra("step2_page_type", "new");
                startActivity(intent4);
                finish();
                break;
            case "You can login, Neighbourhood could not be found then take him to address page":
                // Save all relevant fields from response
                sm.setString("user_id", jsonObject.getString("id"));
                sm.setString("req_ndbstatus", jsonObject.optString("req_ndbstatus", ""));
                sm.setString("referral_status", jsonObject.optString("referral_status", ""));
                sm.setString("refer_neighbourhood_id", jsonObject.optString("refer_neighbourhood_id", ""));
                sm.setString("refer_neighbourhood_name", jsonObject.optString("refer_neighbourhood_name", ""));
                sm.setString("refer_city_name", jsonObject.optString("refer_city_name", ""));
                sm.setString("refer_state_name", jsonObject.optString("refer_state_name", ""));
                sm.setString("refer_country_name", jsonObject.optString("refer_country_name", ""));
                sm.setString("refer_pincode", jsonObject.optString("refer_pincode", ""));
// ✅ Debug Logs (to confirm values are saved)
                Log.d("LoginResponse", "User ID: " + jsonObject.optString("id"));
                Log.d("LoginResponse", "req_ndbstatus: " + jsonObject.optString("req_ndbstatus"));
                Log.d("LoginResponse", "referral_status: " + jsonObject.optString("referral_status"));
                Log.d("LoginResponse", "Neighbourhood ID: " + jsonObject.optString("refer_neighbourhood_id"));
                Log.d("LoginResponse", "Neighbourhood Name: " + jsonObject.optString("refer_neighbourhood_name"));
                Log.d("LoginResponse", "City: " + jsonObject.optString("refer_city_name"));
                Log.d("LoginResponse", "State: " + jsonObject.optString("refer_state_name"));
                Log.d("LoginResponse", "Country: " + jsonObject.optString("refer_country_name"));
                Log.d("LoginResponse", "Pincode: " + jsonObject.optString("refer_pincode"));

                // Redirect to address page
                Intent intent5 = new Intent(LoginActivity.this, SecondPageUserLocationRegisteration.class);
               // intent5.putExtra("step2_page_type", "new"); change to 24oct
                intent5.putExtra("source", "referral");
                startActivity(intent5);
                finish();
                break;

            default:
                welcomeDialog(message);
        }
    }

    private void handleLoginSuccess(JSONObject logindata) throws JSONException {
        LoginPojo loginPojo = new Gson().fromJson(logindata.toString(), LoginPojo.class);
        PrefMananger.saveLoginData(getApplicationContext(), loginPojo);

        sm.setString("user_id", logindata.getInt("id") + "");
        sm.setString("firebase_token", logindata.getString("firebase_token"));
        sm.setString("user_name", logindata.getString("username"));
        sm.setInt("user_verified", logindata.getInt("verified"));
        Log.e("asdf userId", logindata.getInt("id") + "");

        saveAdditionalUserData(logindata);
        int verified = logindata.getInt("verified");
        int getRequest = logindata.getInt("req_ndbstatus");
        if (verified == 1 || verified == 2 || verified == 0) {
            loginSuccessful(verified);
        } else if (getRequest == 3 || getRequest == 4) {
            Intent intent = new Intent(LoginActivity.this, SecondPageUserLocationRegisteration.class);
            intent.putExtra("step2_page_type", "new");
            startActivity(intent);
            finish();
        }
    }

    private void saveAdditionalUserData(JSONObject logindata) throws JSONException {
        sm.setString("neighbrhood", logindata.getString("neighbrshood"));
        sm.setString("neighbrhood_name", logindata.getString("nbd_name"));
        sm.setString("userphoto", logindata.getString("userphoto"));
        sm.setString("state", logindata.getString("state"));
        sm.setString("city", logindata.getString("city"));
        sm.setString("pincode", logindata.getString("pincode"));
        //-11-08-2025 rgn update code remove both key backend side
        // sm.setString("addlineone", logindata.getString("addlineone"));
       // sm.setString("addlinetwo", logindata.getString("addlinetwo"));

        int randomColor = ColorUtils.getRandomColor();
        sm.setInt("randomColor", randomColor);
        Log.d("randomColor", String.valueOf(randomColor));
    }


    private Map<String, String> getLoginParams() {
        Map<String, String> params = new HashMap<>();
        params.put("login", et_email_phone.getText().toString());
        params.put("pass", et_password.getText().toString());
       /* Log.d("sdfsfse", String.valueOf(et_email_phone));
        Log.d("sdfsfse", String.valueOf(et_password));*/
        params.put("mobtoken", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        return params;
    }

    private void loginSuccessful(int verified) {
        showToast("Login successful");
        if (verified == 1) {
            navigateToMainActivity();
        } else {
            PrefMananger.saveScreen(context, "");
            navigateToMainActivity();
        }
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finishAffinity();
    }

    private void navigateToActivity(Class<?> targetActivity) {
        startActivity(new Intent(LoginActivity.this, targetActivity));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    public void welcomeDialog(String message){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.login_error_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(LoginActivity.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        dialog.show();
        FrameLayout frm_choose=dialog.findViewById(R.id.post_frm);
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public static class InternetConnection {

        /**
         * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
         */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr != null) {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

                if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                }
            }
            return false;
        }
    }

    private void clearAppCache() {
        try {
            File dir = getCacheDir();
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (String child : children) {
                    new File(dir, child).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}