package com.app_neighbrsnook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.adapter.NeighbourhoodAdapter;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReferAnNeighbourActivity extends AppCompatActivity {
    private ArrayAdapter<String> neighbourhoodAdapter;
    private ImageView back_btn, search_btn, add_btn;
    private TextView titleTv;
    FrameLayout referButton;
    private EditText phoneEditText;
    private EditText nameEditText;
    private FrameLayout frmNeighbourhood;
    Activity activity;
    Context context;
    SharedPrefsManager sm;
    ProgressDialog progressDialog;
    private TextView itemNeighbrhood;
    private int selectedNeighbourhoodId = -1; // store selected ID
    private String selectedNeighbourhoodName = ""; // store selected name
    private ArrayList<String> neighbourhoodNames = new ArrayList<>();
    private ArrayList<Integer> neighbourhoodIds = new ArrayList<>();
    LinearLayout lnrReferNeigh;
    String  ownerNeighbrhoodId,referrMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_an_neighbour);
        context = activity = this;
        sm = new SharedPrefsManager(this);

        back_btn = findViewById(R.id.back_btn);
        titleTv = findViewById(R.id.title);
        referButton = findViewById(R.id.refer);
        search_btn = findViewById(R.id.search_imageview);
        itemNeighbrhood = findViewById(R.id.itemNeighbrhood);
        add_btn = findViewById(R.id.add_imageview);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        lnrReferNeigh = findViewById(R.id.lnrReferNeigh);
        frmNeighbourhood = findViewById(R.id.frm_neighbourhood);
//        ownerNeighbrsname = sm.getString("neighbrhood_name");
        ownerNeighbrhoodId = sm.getString("neighbrhood");
        referrMsg = sm.getString("referred_msg");
        Log.d("sdsfsfdse",referrMsg);
        titleTv.setText("Refer a Neighbour");
        search_btn.setVisibility(View.GONE);
        add_btn.setVisibility(View.GONE);
       // sm = new SharedPrefsManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        setupPhoneNumberValidation();
        getNeighbourhoodList();
        if (referrMsg != null && !referrMsg.isEmpty()) {
            // Layout ko gone kar do

            lnrReferNeigh.setVisibility(View.GONE);

            // Dialog show karo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Message");
            builder.setMessage(referrMsg);
            builder.setPositiveButton("OK", null); // OK button
            builder.setCancelable(false); // user dialog ke bahar click karke dismiss na kare
            builder.show();
        } else {
            // Agar message null/empty hai toh layout normal dikhe
            lnrReferNeigh.setVisibility(View.VISIBLE);
        }
        itemNeighbrhood.setOnClickListener(v -> {
           showNeighbourhoodDialog();
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    // Form is valid, proceed with referral
                   // submitReferral();
                    String name = nameEditText.getText().toString().trim();
                    String phone = phoneEditText.getText().toString().trim();
                    createReferralApi(name, phone);

                }
            }
        });
    }

    private void setupPhoneNumberValidation() {
        // Maximum 10 digits ka limit set karenge
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(10);
        phoneEditText.setFilters(filters);

        // Only numbers allow karenge
        phoneEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
    }
    private boolean validateForm() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        if (selectedNeighbourhoodId == 0 || selectedNeighbourhoodName == null || selectedNeighbourhoodName.isEmpty()) {
            Toast.makeText(this, "Please select a neighbourhood", Toast.LENGTH_SHORT).show();
            itemNeighbrhood.requestFocus();
            return false;
        }else if (name.isEmpty()) {
            nameEditText.setError("Please enter name");
            nameEditText.requestFocus();
            return false;
        } else {
            nameEditText.setError(null);
        }

        // Phone validation
        if (phone.isEmpty()) {
            phoneEditText.setError("Please enter phone number");
            phoneEditText.requestFocus();
            return false;
        } else if (phone.length() != 10) {
            phoneEditText.setError("Phone number must be 10 digits");
            phoneEditText.requestFocus();
            return false;
        } else if (!Pattern.matches("^[6-9]\\d{9}$", phone)) {
            phoneEditText.setError("Please enter valid phone number");
            phoneEditText.requestFocus();
            return false;
        } else {
            phoneEditText.setError(null);
        }
        // Neighbourhood validation
        return true;
    }


    private void resetForm() {
        // EditTexts clear karna
        nameEditText.setText("");
        phoneEditText.setText("");

        // Focus remove karna
        nameEditText.clearFocus();
        phoneEditText.clearFocus();

        // Keyboard hide karna
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Neighbourhood TextView clear karna
        itemNeighbrhood.setText("Select Neighbourhood");

        // Selected values reset karna
        selectedNeighbourhoodId = 0;
        selectedNeighbourhoodName = "";
    }


    private void createReferralApi(String name, String phone) {
        progressDialog.show();
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("referrer_user_id", Integer.parseInt(sm.getString("user_id")));
            jsonObject.addProperty("referred_name", name);
            jsonObject.addProperty("referred_phone", phone);
            jsonObject.addProperty("neighbourhood_id", selectedNeighbourhoodId);
            jsonObject.addProperty("api", "DEV-3a9f1d2e7b8c4d6f1234abcd5678ef90");
            APIInterface service = new Retrofit.Builder()
                    .baseUrl("https://dev.neighbrsnook.com/admin/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .connectTimeout(3, TimeUnit.MINUTES)
                            .readTimeout(3, TimeUnit.MINUTES)
                            .writeTimeout(3, TimeUnit.MINUTES)
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build())
                    .build()
                    .create(APIInterface.class);

            Call<JsonElement> call = service.createReferral(jsonObject);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    progressDialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONObject json = new JSONObject(response.body().toString());
                            boolean success = json.optBoolean("success");
                            String message = json.optString("message", "Something went wrong.");

                            if (success) {
                                JSONObject data = json.optJSONObject("data");
                                if (data != null) {
                                    String referralCode = data.optString("referral_code", "N/A");
                                    String referredName = data.optString("referred_name", name);
                                    String referredPhone = data.optString("referred_phone", phone);
                                    String neighbourhoodName = "Neighbourhood ID: " + data.optInt("neighbourhood_id", 104);

                                    // ✅ API se refer_subject aur refer_message le rahe hain
                                    String referSubject = data.optString("refer_subject", "Refer a Neighbour on Neighbrsnook");
                                    String referMessage = data.optString("refer_message",
                                            "Hi! I am referring you to join me on Neighbrsnook - it's a safe, real-neighbour app that helps us stay connected, exchange info, and make our community stronger.\n\n" +
                                                    "You can download it here:\nhttps://neighbrsnook.com/open-app");

                                    Toast.makeText(ReferAnNeighbourActivity.this,
                                            "Referral Created Successfully!", Toast.LENGTH_SHORT).show();

                                    // Share using API message
                                    submitReferralShare(referSubject, referMessage);

                                    resetForm();
                                } else {
                                    Toast.makeText(ReferAnNeighbourActivity.this,
                                            "Invalid data received!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ReferAnNeighbourActivity.this,
                                    "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // ✅ HTTP error case me readable message
                        String errorMsg = "Something went wrong. Please try again.";
                        try {
                            if (response.errorBody() != null) {
                                JSONObject errorJson = new JSONObject(response.errorBody().string());
                                errorMsg = errorJson.optString("message", errorMsg);
                            }
                        } catch (Exception ignored) {}
                        Toast.makeText(ReferAnNeighbourActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ReferAnNeighbourActivity.this,
                            "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*private void submitReferralShare(String name, String phone, String neighbourhood, String referralCode) {
        StringBuilder message = new StringBuilder();
        *//*message.append("🎉 *Refer a Neighbour!*\n\n");
        message.append("Name: ").append(name).append("\n");
        message.append("Phone: ").append(phone).append("\n");
        message.append(neighbourhood).append("\n");
        message.append("Referral Code: ").append(referralCode).append("\n\n");
        message.append("I'm referring this person to join our community on Neighbrsnook!");
*//*
        message.append("Hi! I am referring you to join me on Neighbrsnook - its a safe, real-neighbour app that helps us stay connected, exchange info, and make our community stronger.\n" +
                "you can download it here:");
        message.append("https://neighbrsnook.com/open-app");
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Refer a Neighbour");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }*/
    private void submitReferralShare(String subject, String messageText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageText);

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void getNeighbourhoodList() {
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);

        int userId = Integer.parseInt(sm.getString("user_id")); // Session se user id le lo
        String search = ""; // agar search field hai to uski value bhejo
        String apiKey = "DEV-3a9f1d2e7b8c4d6f1234abcd5678ef90";


        Call<JsonElement> call = service.searchNeighbourhoodbyNearby(userId, search, apiKey);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject json = new JSONObject(response.body().toString());
                        boolean success = json.optBoolean("success");
                        if (success) {
                            JSONArray dataArray = json.optJSONArray("data");

                            neighbourhoodNames.clear();
                            neighbourhoodIds.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                String name = obj.optString("name");
                                int id = obj.optInt("id"); // id ko int me store karo

                                neighbourhoodNames.add(name);
                                neighbourhoodIds.add(id);
                            }

                        } else {
                            Toast.makeText(ReferAnNeighbourActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ReferAnNeighbourActivity.this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReferAnNeighbourActivity.this, "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(ReferAnNeighbourActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("NeighbourhoodAPI", t.getMessage());
            }
        });
    }
    private void showNeighbourhoodDialog() {
        if (neighbourhoodNames.isEmpty()) {
            Toast.makeText(this, "Neighbourhood list is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.open_profession_step2); // same layout as aapne category ke liye use kiya
        RecyclerView rv = dialog.findViewById(R.id.rv_category);
        ImageView cancel = dialog.findViewById(R.id.cross_imageview);
        TextView tv_itm = dialog.findViewById(R.id.tv_itm);
        tv_itm.setText("Select Neighbourhood");

        rv.setLayoutManager(new LinearLayoutManager(this));

        NeighbourhoodAdapter adapter = new NeighbourhoodAdapter(neighbourhoodNames, neighbourhoodIds, new NeighbourhoodAdapter.NeighbourhoodInterface() {
            @Override
            public void onNeighbourhoodSelected(String name, int id) {
                selectedNeighbourhoodName = name;
                selectedNeighbourhoodId = id;
                itemNeighbrhood.setText(name);
                dialog.dismiss();
            }
        });

        rv.setAdapter(adapter);

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));
        dialog.setCancelable(true);
        dialog.show();
    }

}
