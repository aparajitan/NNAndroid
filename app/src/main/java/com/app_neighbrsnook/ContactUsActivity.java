package com.app_neighbrsnook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {
    ImageView back_btn;
    TextView titleTv, send_textview, first_name_tv, last_name_tv;
    EditText editText, email_tv, mobile_tv;
    ImageView serch_btn, add_btn;
    SharedPrefsManager sm;
    ProgressDialog progressDialog;
    LinearLayout root;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        sm = new SharedPrefsManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        send_textview = findViewById(R.id.send_textview);
        editText = findViewById(R.id.message_et);
        first_name_tv = findViewById(R.id.first_name_tv);
        last_name_tv = findViewById(R.id.last_name_tv);
        email_tv = findViewById(R.id.et_email);
        mobile_tv = findViewById(R.id.et_mobile);
        serch_btn = findViewById(R.id.search_imageview);
        root = findViewById(R.id.root);
        add_btn = findViewById(R.id.add_imageview);
        serch_btn.setVisibility(View.GONE);
        add_btn.setVisibility(View.GONE);
        titleTv.setText("Contact us");

        userProfile();
        editText.requestFocus();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard1();
                return false;
            }
        });

        send_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckAllFields()) {
                    progressDialog.show();
                    contactApi();
                }
            }
        });
    }

    private void contactApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("phoneno", mobile_tv.getText().toString());
        hm.put("emailid", email_tv.getText().toString());
        hm.put("textmessage", editText.getText().toString());

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.contactUs("contactus", hm);
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
//                categorylist = response.body().getNbdata();
                openDialog();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(ContactUsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.global_dialog);
        TextView msg_textview = dialog.findViewById(R.id.msg);
        msg_textview.setText("Your message has been sent. We will review your concern and revert shortly.");
        TextView ok = dialog.findViewById(R.id.ok_textview);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideKeyboard1() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void userProfile() {
        HashMap<String, Object> hm = new HashMap<>();
       // hm.put("userid", Integer.parseInt(PrefMananger.GetLoginData(this).getId() + ""));
        hm.put("loggeduser", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(sm.getString("user_id")), hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement = response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                try {

                    first_name_tv.setText(jsonObject.get("username").getAsString());
                    email_tv.setText(jsonObject.get("emailid").getAsString());
                    String tv_phone = jsonObject.get("phoneno").getAsString();
                    mobile_tv.setText(tv_phone);

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
//                AppCommon.getInstance(PlaceOrderActivity.this).clearNonTouchableFlags(PlaceOrderActivity.this);
                Toast.makeText(ContactUsActivity.this, "Data found", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private boolean CheckAllFields() {
        String message = editText.getText().toString();

        if (message.matches("")) {
            return GlobalMethods.setError(editText, "Please enter your message");
        } else if (BadWordFilter.containsBadWord(message)) {
            GlobalMethods.getInstance(ContactUsActivity.this)
                    .globalDialogAbusiveWord(ContactUsActivity.this, getString(R.string.abusive_msg));
            return false;
        }

        return true;
    }

}