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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
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

    private Spinner neighbourhoodSpinner;
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
        frmNeighbourhood = findViewById(R.id.frm_neighbourhood);
        neighbourhoodSpinner = findViewById(R.id.neighbourhoodSpinner);

        titleTv.setText("Refer a Neighbour");
        search_btn.setVisibility(View.GONE);
        add_btn.setVisibility(View.GONE);
       // sm = new SharedPrefsManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        setupPhoneNumberValidation();
        setupNeighbourhoodSpinner();
        getNeighbourhoodList();

        itemNeighbrhood.setOnClickListener(v -> {
           showNeighbourhoodDialog();
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        frmNeighbourhood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Spinner open karenge
                neighbourhoodSpinner.performClick();
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

    private void setupNeighbourhoodSpinner() {
        // Data list banayenge
        List<String> neighbourhoodsList = new ArrayList<>();
        neighbourhoodsList.add("Select neighbourhood"); // Hint as first item
        neighbourhoodsList.add("Sector 15");
        neighbourhoodsList.add("Sector 15A");
        neighbourhoodsList.add("Sector 16");
        neighbourhoodsList.add("Sector 16A");
        neighbourhoodsList.add("Sector 18");
        neighbourhoodsList.add("Sector 18A");

        // Create custom adapter for Spinner with hint text color
        neighbourhoodAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                neighbourhoodsList
        ) {
            @Override
            public boolean isEnabled(int position) {
                // First item (hint) selectable nahi hoga
                return position != 0;
            }

            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if (position == 0) {
                    // Hint text gray color mein
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    // Normal text black color mein
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                // Hint item ko dropdown mein disable karenge
                if (position == 0) {
                    textView.setEnabled(false);
                    textView.setClickable(false);
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };

        // Dropdown layout set karenge
        neighbourhoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter ko Spinner mein set karenge
        neighbourhoodSpinner.setAdapter(neighbourhoodAdapter);

        // Initially hint show karenge
        neighbourhoodSpinner.setSelection(0, false);

        // Spinner item selection handle karenge
        neighbourhoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedNeighbourhood = (String) parent.getItemAtPosition(position);
                    // Text color black karenge jab kuch select ho jaye
                    if (view != null) {
                        TextView textView = (TextView) view;
                        textView.setTextColor(getResources().getColor(android.R.color.black));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Kuch select nahi hua toh
            }
        });
    }

    private boolean validateForm() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
       // int neighbourhoodPosition = neighbourhoodSpinner.getSelectedItemPosition();

        // Name validation
        if (name.isEmpty()) {
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

    private void submitReferral() {
        // Get all form data
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String neighbourhood = neighbourhoodSpinner.getSelectedItem().toString();

        // Create share message
        String shareMessage = createShareMessage(name, phone, neighbourhood);

        // Share intent create karenge
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Refer a Neighbour");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        // Share dialog show karenge
        startActivity(Intent.createChooser(shareIntent, "Share via"));

        // Reset form if needed
        resetForm();
    }

    private String createShareMessage(String name, String phone, String neighbourhood) {
        StringBuilder message = new StringBuilder();
        message.append("Refer a Neighbour\n\n");
        message.append("Name: ").append(name).append("\n");
        message.append("Phone: ").append(phone).append("\n");
        message.append("Neighbourhood: ").append(neighbourhood).append("\n\n");
        message.append("I'm referring this person to join our community!");

        return message.toString();
    }

    private void resetForm() {
        nameEditText.setText("");
        phoneEditText.setText("");
        neighbourhoodSpinner.setSelection(0);
    }

    private void createReferralApi1(String name, String phone) {
        progressDialog.show();

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("referrer_user_id", Integer.parseInt(sm.getString("user_id"))); // Session se user id
            jsonObject.addProperty("referred_name", name);
            jsonObject.addProperty("referred_phone", phone);
           // jsonObject.addProperty("referred_email", "arsadli676@gmail.com"); // static email
            jsonObject.addProperty("neighbourhood_id", 104); // static neighbourhood id
          //  jsonObject.addProperty("remarks", "Friend from college"); // static remarks
            jsonObject.addProperty("api", "DEV-3a9f1d2e7b8c4d6f1234abcd5678ef90"); // static API key

            APIInterface service = new Retrofit.Builder()
                    .baseUrl("https://dev.neighbrsnook.com/admin/api/")  // ✅ Correct base URL
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
                            String message = json.optString("message");

                            if (success) {
                                JSONObject data = json.optJSONObject("data");
                                String referralCode = data.optString("referral_code", "N/A");
                                Toast.makeText(ReferAnNeighbourActivity.this, "Referral Created Successfully!\nCode: " + referralCode, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ReferAnNeighbourActivity.this,
                                        message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ReferAnNeighbourActivity.this,
                                    "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ReferAnNeighbourActivity.this,
                                "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ReferAnNeighbourActivity.this,
                            "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void createReferralApi(String name, String phone) {
        progressDialog.show();

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("referrer_user_id", Integer.parseInt(sm.getString("user_id"))); // user id
            jsonObject.addProperty("referred_name", name);
            jsonObject.addProperty("referred_phone", phone);
            //jsonObject.addProperty("referred_email", "arsadli6361@gmail.com"); // static
           // jsonObject.addProperty("neighbourhood_id", 104); // static
            jsonObject.addProperty("neighbourhood_id", selectedNeighbourhoodId);
          //  jsonObject.addProperty("remarks", "Friend from college"); // static
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
                            String message = json.optString("message");

                            if (success) {
                                JSONObject data = json.optJSONObject("data");
                                String referralCode = data.optString("referral_code", "N/A");
                                String referredName = data.optString("referred_name", name);
                                String referredPhone = data.optString("referred_phone", phone);
                                String neighbourhoodName = "Neighbourhood ID: " + data.optInt("neighbourhood_id", 104);

                                Toast.makeText(ReferAnNeighbourActivity.this,
                                        "Referral Created Successfully!", Toast.LENGTH_SHORT).show();

                                // ✅ Now trigger share intent
                                submitReferralShare(referredName, referredPhone, neighbourhoodName, referralCode);

                                // ✅ Reset form
                                resetForm();

                            } else {
                                Toast.makeText(ReferAnNeighbourActivity.this,
                                        message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ReferAnNeighbourActivity.this,
                                    "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ReferAnNeighbourActivity.this,
                                "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ReferAnNeighbourActivity.this,
                            "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void submitReferralShare(String name, String phone, String neighbourhood, String referralCode) {
        StringBuilder message = new StringBuilder();
        /*message.append("🎉 *Refer a Neighbour!*\n\n");
        message.append("Name: ").append(name).append("\n");
        message.append("Phone: ").append(phone).append("\n");
        message.append(neighbourhood).append("\n");
        message.append("Referral Code: ").append(referralCode).append("\n\n");
        message.append("I'm referring this person to join our community on Neighbrsnook!");
*/
        message.append("Hi! I am referring you to join me on Neighbrsnook - its a safe, real-neighbour app that helps us stay connected, exchange info, and make our community stronger.\n" +
                "you can download it here:");
        message.append("https://neighbrsnook.com/open-app");
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Refer a Neighbour");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

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
        tv_itm.setText("Select Neighbrhood");

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
