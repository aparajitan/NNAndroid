package com.app_neighbrsnook.registration;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.adapter.CountryAdapter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.RegistrationGenderAdapter;
import com.app_neighbrsnook.adapter.SelectionNeighbrhoodAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.NeighbhoodAddressModel;
import com.app_neighbrsnook.pojo.ReviewPojo;
import com.app_neighbrsnook.pojo.StateDropdownHomePojo;
import com.app_neighbrsnook.pojo.StateDropdownPojo;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.profile.ProfileUpdateDocumentUser;
import com.app_neighbrsnook.utils.DeviceUtils;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.MetaEventLogger;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondPageUserLocationRegisteration extends AppCompatActivity implements View.OnClickListener, RegistrationGenderAdapter.CategoryInterface1, CountryAdapter.OnItemSelected, ImageUploadAdapter.ImageRequest, LocationListener {

    FrameLayout frm_upload, uload_reach_out, frmAdharCard, frmPassport, frmVoterId, frmDl;
    ImageView img_back_address_proof, imgVoterFront, imgVotertBack;
    LinearLayout frm_aadhar_card, lnr_passport, lnr_voter_id;
    //ActivityAddressProofBinding binding;
    Context context;
    RecyclerView recy_selecy_neighbrhood;
    Calendar calendar;
    RelativeLayout photo_dialog_open_rl;
    RadioGroup rgIdProof;
    Dialog mail_dialog, interestDialog;
    int type = 1;
    Dialog image_dialog;
    private static int AUTOCOMPLETE_REQUEST_CODE = 10;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest locationRequest;

    SelectionNeighbrhoodAdapter selectionNeighbrhoodAdapter;
    TextView yourNeighbrhood;
    int PERMISSION_ID = 44;
    TextView areaLocation, addressTv, useCurrentLocation;
    String stLocation;
    LinearLayout currentLocationIcon;
    Activity activity;
    ProgressBar progressBar;
    EditText edt_address1, edt_address2;
    CountryAdapter countryAdapter;
    TextView country_tv,edt_pincode, city_tv, state_tv, currentLocationIconTv;
    String countryName, stateName, cityName;
    List<Nbdatum> statelist = new ArrayList<>();
    List<Nbdatum> citylist = new ArrayList<>();
    List<Nbdatum> countryList = new ArrayList<>();
    Dialog country_dialog;
    int countryid, cityId, stateid, nbdId;
    HashMap<String, Object> hm;
    SharedPrefsManager sm;
    ImageView adhar_front_img, adhar_back_img, passport_front, passport_back, voter_front, voter_back, imgDlFront, imgDltBack;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    ImageUploadAdapter uploadAdapter;
    List<String> adharImageList = new ArrayList<>();
    private Context mContext = SecondPageUserLocationRegisteration.this;
    EditText edt_area1;
    String nbdNamearea = "";
    FrameLayout frmStepId, genderFrm, dobFrm;
    private static final int REQUEST = 112;
    ArrayList<StateDropdownPojo> selectNeighbrhood = new ArrayList<>();
    TextView tv, changeLocationTv, select_gender, tvDateofBirth;
    String value;
    LinearLayout location_child, lnrNeighbrhoodUi, location_childChangelocation;
    ImageView imgAdharFront, imgAdharBack, imgPassportFront, imgPassportBack;

    String nghString;
    HashMap<String, Object> hashMap;
    LinearLayout lytParent, lnrParentLocationNext, lnrBack, lnrFront;
    RadioButton rdAdharCard, rdPassport, rdVoter, rdDl, rentLease;
    private FirebaseAnalytics mFirebaseAnalytics;
    CommonPojoSuccess statusPojo;
    TextView tvYourAddressProofNgh, tvNghidAddress;
    double latitude = 0.0;
    double longitude = 0.0;
    //  String address = "";
    String areaName = "";
    String area;
    private static final Pattern AADHAAR_PATTERN = Pattern.compile("\\b\\d{4}\\s\\d{4}\\s\\d{4}\\b");
    String st_city, st_state, st_area, st_pincode;
    private boolean isLocationAlreadySet = false;
    private static final String COUNTRY_ID = "100";
    private static final String SUCCESS_STATUS = "success";
    String stAreaforDialog;
    TextView tvYourNeighbrhood;
   // private boolean isManualLocationSelected = false;

    private static final String PREFS_NAME = "AddressProofPrefs";
    private static final String KEY_MANUAL_SELECTED = "manualSelected";
    private boolean isManualLocationSelected;
    String area1;
    String cityGet,stateGet,pincodeGet,stUploadDoc;
    TextView tvNext;
    String profileNeighbrhood,source,stAddressProof,stAddress,locationNeighbrhood;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        sm = new SharedPrefsManager(this);
        calendar = Calendar.getInstance();
        setContentView(R.layout.activity_address_proof_location);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isManualLocationSelected = prefs.getBoolean(KEY_MANUAL_SELECTED, false);
        String step2PageType = getIntent().getStringExtra("step2_page_type");
        boolean isNewRegistration = "new".equals(step2PageType);
        init();

        if (isNewRegistration) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(KEY_MANUAL_SELECTED);
            editor.apply();
            isManualLocationSelected = false;
        }

        if (!checkPermissions() && !isManualLocationSelected && isNewRegistration) {
            showLocationPermissionDialog();
        }


// ✅ Extras handle safely
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            source = extras.getString("source", ""); // "profile", "registration", or "referral"
            if ("profile".equalsIgnoreCase(source)) {
                // 🧾 PROFILE CASE
                profileNeighbrhood = extras.getString("neighbrhood", "");
                cityGet = extras.getString("city", "");
                stateGet = extras.getString("state", "");
                pincodeGet = extras.getString("pincode", "");
                stAddress = extras.getString("addres", "");
                stAddressProof = extras.getString("addressone", "");
                locationNeighbrhood = extras.getString("neighbrhood", "");
                stUploadDoc = extras.getString("stUploadDocument", "");

                state_tv.setText(stateGet);
                edt_pincode.setText(pincodeGet);
                city_tv.setText(cityGet);
                areaLocation.setText(profileNeighbrhood);
                addressTv.setText(stAddress);
                edt_address1.setText(stAddressProof);
                area1 = profileNeighbrhood;
                Log.d("ProfileData", "Neighbourhood: " + profileNeighbrhood);
                frm_upload.setVisibility(VISIBLE);
                tvNext.setText("Next");
                getSelectNeighbourHoodList(false);

            }
            else if ("wall".equalsIgnoreCase(source)) {
                // 🧾 NORMAL REGISTRATION
                profileNeighbrhood = extras.getString("neighbrhood", "");
                cityGet = extras.getString("city", "");
                stateGet = extras.getString("state", "");
                pincodeGet = extras.getString("pincode", "");
                stAddress = extras.getString("addres", "");
                stAddressProof = extras.getString("addressone", "");
                locationNeighbrhood = extras.getString("locationNeighbrhood", "");
                stUploadDoc = extras.getString("stUploadDocument", "");

                state_tv.setText(stateGet);
                edt_pincode.setText(pincodeGet);
                city_tv.setText(cityGet);
                areaLocation.setText(locationNeighbrhood);
                addressTv.setText(stAddress);
                edt_address1.setText(stAddressProof);
                area1 = profileNeighbrhood;

                frm_upload.setVisibility(VISIBLE);
                tvNext.setText("Next");
                Log.d("SourceType", "Normal Registration");
                getSelectNeighbourHoodList(false);

            }
            else if ("referral".equalsIgnoreCase(source)) {

                // 🧾 REFERRAL REGISTRATION CASE
                String neighbourhoodName = sm.getString("refer_neighbourhood_name", "");
                String cityName = sm.getString("refer_city_name", "");
                String stateName = sm.getString("refer_state_name", "");
                String countryName = sm.getString("refer_country_name", "");
                String pincode = sm.getString("refer_pincode", "");

                Log.d("ReferralData",
                        "Neighbourhood: " + neighbourhoodName + ", City: " + cityName +
                                ", State: " + stateName + ", Country: " + countryName + ", Pincode: " + pincode);

                // 🔹 Only set UI if data is not empty
                if (!neighbourhoodName.isEmpty()) areaLocation.setText(neighbourhoodName);
                if (!cityName.isEmpty()) city_tv.setText(cityName);
                if (!stateName.isEmpty()) state_tv.setText(stateName);
                if (!pincode.isEmpty()) edt_pincode.setText(pincode);

                // 🧹 Make sure upload section visible
                frm_upload.setVisibility(VISIBLE);
                tvNext.setText("Next");
                getSelectNeighbourHoodList(true);

            }
            else {
                // 🧾 Default / Fallback (Blank state)
                state_tv.setText("");
                edt_pincode.setText("");
                city_tv.setText("");
                areaLocation.setText("");
                area1 = "";
                frm_upload.setVisibility(VISIBLE);
            }

        } else {
            // 🧾 First Time Registration (Extras null)
            source = "registration";
            state_tv.setText("");
            edt_pincode.setText("");
            city_tv.setText("");
            areaLocation.setText("");
            area1 = "";
            frm_upload.setVisibility(VISIBLE);
        }


// ✅ Baaki aapka original code
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        PrefMananger.saveScreen(context, PrefMananger.ADDRESS_PROOF);
        stateApi("100");
        state_tv.setOnClickListener(this);
        city_tv.setOnClickListener(this);
        frmStepId.setVisibility(GONE);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        callDeviceInfoApi();
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "manual_debug");
        FirebaseAnalytics.getInstance(this).logEvent("test_event", bundle);
        if (source.equals("profile")){
           // getLastLocation();
        }else if (source.equals("wall")){
            //getLastLocation();
        }else if (source.equals("referral")){
            //getLastLocation();
        }else {
            getLastLocation();
        }

// ✅ Listeners jo aapke code me the
        currentLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
        edt_address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_address1.requestFocus();
                edt_address1.setCursorVisible(true);
            }
        });
        currentLocationIconTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
       /* frm_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckAllFields()) {
                    addressProofDocumentSubmit();
                    Intent intent;
                    if ("profile".equalsIgnoreCase(source)) {
                        intent = new Intent(SecondPageUserLocationRegisteration.this, ProfileUpdateDocumentUser.class);
                        intent.putExtra("neighbrhood", locationNeighbrhood);
                        intent.putExtra("stUploadDocument", stUploadDoc);
                        Log.d("sdfsfaqqqewewe",stUploadDoc);
                        intent.putExtra("source", "profile");
                    } else if ("wall".equalsIgnoreCase(source)) {
                        intent = new Intent(SecondPageUserLocationRegisteration.this, ProfileUpdateDocumentUser.class);
                        intent.putExtra("stUploadDocument", stUploadDoc);
                        Log.d("sdfsfaewewe",stUploadDoc);
                        intent.putExtra("source", "wall");
                    } else {
                        intent = new Intent(SecondPageUserLocationRegisteration.this, LastPageUserDocumentRegisteration.class);
                        intent.putExtra("source", "unknown"); // fallback case
                    }

                    // Agar aapko hamesha AddressDocumentDemo par hi jana hai,
                    // toh upar ki conditions hata kar sirf yeh rakho:
                    // intent = new Intent(AddressProofLocation.this, AddressDocumentDemo.class);

                    startActivity(intent);
                }
            }
        });*/
        frm_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAllFields()) {
                    // ✅ Only call API, no intent here
                    addressProofDocumentSubmit();
                }
            }
        });

        uload_reach_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    reachOutBtn();
            }
        });

        img_back_address_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackConfirmationDialog();
            }
        });

        location_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondPageUserLocationRegisteration.this, AddressLocationPage.class);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        location_childChangelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .setHint("Area Search")
                        .build(SecondPageUserLocationRegisteration.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        lnrParentLocationNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondPageUserLocationRegisteration.this, AddressLocationPage.class);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        changeLocationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .setHint("Area Search")
                        .build(SecondPageUserLocationRegisteration.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        frm_upload.setVisibility(VISIBLE);
        country_tv.setText("India");


    }

    private void getSelectNeighbourHoodList(boolean isCurrentLocation) {
        UtilityFunction.showLoading(context, "Please wait...");
        area = areaLocation.getText().toString();
        String finalArea;
        if ("profile".equalsIgnoreCase(source)) { // NullPointerException avoid karne ke liye
            finalArea = area; // area1 pass karega jab source "profile" ho
        } else {
            finalArea = area; // area pass karega jab else condition ho
        }
//        String url = UrlClass.SELECT_NEIGHBRHOOD + "&areas=" + finalArea + "&lati=" + latitude + "&longi=" + longitude;

        StringBuilder urlBuilder = new StringBuilder(UrlClass.SELECT_NEIGHBRHOOD);
        urlBuilder.append("&areas=").append(finalArea);

        if (latitude != 0.0) {
            urlBuilder.append("&lati=").append(latitude);
        }
        if (longitude != 0.0) {
            urlBuilder.append("&longi=").append(longitude);
        }

        String url = urlBuilder.toString();
        ApiExecutor.getApiService().getDropdownData(url).enqueue(new Callback<StateDropdownHomePojo>() {
            @Override
            public void onResponse(Call<StateDropdownHomePojo> call, Response<StateDropdownHomePojo> response) {
                UtilityFunction.hideLoading();
                if (response.isSuccessful()) {
                    StateDropdownHomePojo responseBody = response.body();
                    if (responseBody != null && "success".equals(responseBody.status)) {
                        neighborhoodLocationStatusUnified(1);
                        handleSuccessfulResponse(responseBody);
                        logRegistrationEvent("neighborhood_discovered", "search_neighborhood_call");
                    } else if ("failure".equals(responseBody.status)) {
                        handleFailureResponse();
                    } /*else {
                        handleEmptyResponse();
                    }*/
                } else {
                    handleEmptyResponse();
                }
            }

            @Override
            public void onFailure(Call<StateDropdownHomePojo> call, Throwable t) {
                UtilityFunction.hideLoading();
                handleFailureResponse();
            }
        });
    }

    private void handleSuccessfulResponse(StateDropdownHomePojo responseBody) {
        selectNeighbrhood = responseBody.data;
        if (selectNeighbrhood != null && !selectNeighbrhood.isEmpty()) {
            setUpDataList();
            if (selectNeighbrhood.size() == 1) {
                areaName = selectNeighbrhood.get(0).nbd_name;
                nbdId = Integer.parseInt(selectNeighbrhood.get(0).nbdId);
                edt_area1.setText(areaName); //26-11-24
                selectionNeighbrhoodAdapter.selected = 0;
                selectionNeighbrhoodAdapter.notifyDataSetChanged();
                lnrNeighbrhoodUi.setVisibility(VISIBLE);
            }else {
                lnrNeighbrhoodUi.setVisibility(VISIBLE);
            }
            uload_reach_out.setVisibility(GONE);
            recy_selecy_neighbrhood.setVisibility(VISIBLE);
            frm_upload.setVisibility(VISIBLE);
        } else {
            handleEmptyResponse();
        }
    }

    private void handleFailureResponse() {
        neighborhoodLocationStatusUnified(0);
        resetUI();
    }

    private void handleEmptyResponse() {
        selectNeighbrhood = new ArrayList<>();
        setUpDataList();
        resetUI();
    }

    private void resetUI() {
        recy_selecy_neighbrhood.setVisibility(GONE);
        uload_reach_out.setVisibility(VISIBLE);
        frm_upload.setVisibility(GONE);
        lnrNeighbrhoodUi.setVisibility(GONE);
        yourNeighbrhood.setVisibility(VISIBLE);
       // edt_area1.setText("");27-08-2025
        //edt_area1.setText(""); reachout me null ja rha tha area iski wajah se
    }

    private void setUpDataList() {
        if (selectNeighbrhood == null) {
            selectNeighbrhood = new ArrayList<>();
        }
        recy_selecy_neighbrhood.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        selectionNeighbrhoodAdapter = new SelectionNeighbrhoodAdapter(context, selectNeighbrhood, new SelectionNeighbrhoodAdapter.ItemClick() {
            @Override
            public void onItemClick(int position) {
                nbdId = Integer.parseInt(selectNeighbrhood.get(position).nbdId);
                areaName = selectNeighbrhood.get(position).getNbd_name();
                // edt_area1.setText(areaName);
                edt_area1.setText(areaName);
            }
        });
        recy_selecy_neighbrhood.setAdapter(selectionNeighbrhoodAdapter);
        selectionNeighbrhoodAdapter.notifyDataSetChanged();
        edt_area1.setText("");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.country_tv:
                //listOfCountrt(countryList,"country");
                break;
            case R.id.state_address_proof:
                // listOfCountrt(statelist,"state");
                break;
            case R.id.city_address_proof:
                //listOfCountrt(citylist,"city");
                break;
        }
    }

    @Override
    public void onSelected(int position, String name, String id, String type) {
        try {
            int parsedId = Integer.parseInt(id);

            switch (type) {
                case "country":
                    country_tv.setText(name);
                    countryName = name;
                    countryid = parsedId;
                    stateApi(id);
                    break;

                case "state":
                    state_tv.setText(name);
                    stateName = name;
                    stateid = parsedId;
                    cityApi(parsedId);
                    break;

                case "city":
                    city_tv.setText(name);
                    cityName = name;
                    cityId = parsedId;
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Log the error for debugging
            // Optionally, show an error message to the user
        } finally {
            country_dialog.dismiss();
        }
    }

    public void welcomeDialogReachout(String message) {
        final Dialog dialog = new Dialog(SecondPageUserLocationRegisteration.this);
        dialog.setContentView(R.layout.reachout_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SecondPageUserLocationRegisteration.this,
                android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        FrameLayout frm_choose = dialog.findViewById(R.id.post_frm);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        dialog.show();
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // PrefMananger.saveScreen(context,"");
                startActivity(new Intent(SecondPageUserLocationRegisteration.this, LoginActivity.class));
                finish();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                if (data != null) {
                    String placeName = data.getStringExtra("place_name");
                    String placeAddress = data.getStringExtra("place_address");
                    double latitude = data.getDoubleExtra("latitude", 0.0);
                    double longitude = data.getDoubleExtra("longitude", 0.0);

                    if (placeName != null) areaLocation.setText(placeName);
                    if (placeAddress != null) addressTv.setText(placeAddress);

                    Log.d("sdsdfsdfae", areaLocation.getText().toString());
                    getAddress(latitude, longitude);
                    getSelectNeighbourHoodList(false);
                }
            }
        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
            Status status = Autocomplete.getStatusFromIntent(data);
            if (status != null && status.getStatusMessage() != null) {
                Log.i("TAG_status", status.getStatusMessage());
            }
        }
    }



    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) {
        File file = null;
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getString(R.string.app_name);
            File directory = new File(path);

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave);
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file; // it will return null
        }
    }
    @Override
    public void onImageClick(int pos) {
        bitmapList.remove(pos);
        uploadAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrossClick(int pos, String from) {
    }

    @Override
    public void onCrossClick1(int pos, String from) {
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    private void getAddress(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address addressObj = addresses.get(0);
                // Get address components
                String address = addressObj.getAddressLine(0);
                String state = addressObj.getAdminArea();
                String postalCode = addressObj.getPostalCode();
                String subLocality = addressObj.getSubLocality();
                String city = addressObj.getLocality();
                String country = addressObj.getCountryName();
                // Remove state, postal code, city, and country from the address string
                if (state != null) {
                    address = address.replace(state, "").trim(); // Remove the state
                }
                if (postalCode != null) {
                    address = address.replace(postalCode, "").trim(); // Remove the postal code
                }
                if (city != null) {
                    address = address.replace(city, "").trim(); // Remove the city
                }
                if (country != null) {
                    address = address.replace(country, "").trim(); // Remove the country (e.g., "India")
                }
                // Further clean up to remove extra commas, spaces, and newlines
                address = address.replaceAll("\\s*,\\s*", ", ");  // Remove extra commas and spaces
                address = address.replaceAll("\\s*\\n\\s*", " ");  // Remove extra newlines
                address = address.replaceAll(",\\s*$", "");  // Remove trailing commas
                address = address.replaceAll(",\\s*$", "");  // Remove trailing commas
                address = address.replaceAll(",\\s*$", "");  // Remove trailing commas
                address = address.replaceAll(",+", ",");
                stAreaforDialog = subLocality;
                Log.d("Cleaned Address", address);
                // Set the cleaned address in the TextView
                addressTv.setText(address);
              /*  tvYourAddressProofNgh.setText("(For " + subLocality + ")");
                tvNghidAddress.setText("(For " + subLocality + ")");*/


                if (subLocality != null && !subLocality.trim().isEmpty()) {
                    tvYourAddressProofNgh.setVisibility(VISIBLE);
                    tvNghidAddress.setVisibility(VISIBLE);

                    tvYourAddressProofNgh.setText("(For " + subLocality + ")");
                    tvNghidAddress.setText("(For " + subLocality + ")");
                } else {
                    tvYourAddressProofNgh.setVisibility(GONE);
                    tvNghidAddress.setVisibility(GONE);
                }

                edt_pincode.setText(postalCode);
                edt_area1.setText(subLocality);
                state_tv.setText(state);
                city_tv.setText(city);

                getSelectNeighbourHoodList(true);
            } else {
                Log.d("Address", "No address found for the given coordinates.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Geocoder Error", "Unable to retrieve address.");
        }
    }

    private void getAddress1(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address addressObj = addresses.get(0);
                // Get address components
                String address = addressObj.getAddressLine(0);
                String state = addressObj.getAdminArea();
                String postalCode = addressObj.getPostalCode();
                String subLocality = addressObj.getSubLocality();
                String city = addressObj.getLocality();
                String country = addressObj.getCountryName();
                // Remove state, postal code, city, and country from the address string
                if (state != null) {
                    address = address.replace(state, "").trim(); // Remove the state
                }
                if (postalCode != null) {
                    address = address.replace(postalCode, "").trim(); // Remove the postal code
                }
                if (city != null) {
                    address = address.replace(city, "").trim(); // Remove the city
                }
                if (country != null) {
                    address = address.replace(country, "").trim(); // Remove the country (e.g., "India")
                }
                // Further clean up to remove extra commas, spaces, and newlines
                address = address.replaceAll("\\s*,\\s*", ", ");  // Remove extra commas and spaces
                address = address.replaceAll("\\s*\\n\\s*", " ");  // Remove extra newlines
                address = address.replaceAll(",\\s*$", "");  // Remove trailing commas
                address = address.replaceAll(",\\s*$", "");  // Remove trailing commas
                address = address.replaceAll(",\\s*$", "");  // Remove trailing commas
                address = address.replaceAll(",+", ",");

                // Log the cleaned address

                if (source.equals("profile")){
                    state_tv.setText(stateGet);
                    edt_pincode.setText(pincodeGet);
                    city_tv.setText(cityGet);
                    areaLocation.setText(area1);
                    addressTv.setText(stAddress);
                   // edt_address1.setText(stAddressProof);
                } else if (source.equals("wall")){
                    state_tv.setText(stateGet);
                    edt_pincode.setText(pincodeGet);
                    city_tv.setText(cityGet);
                    areaLocation.setText(locationNeighbrhood);
                    addressTv.setText(stAddress);
                    edt_address1.setText(stAddressProof);
                }else {
                    addressTv.setText(address);
                    areaLocation.setText(subLocality);
                    edt_pincode.setText(postalCode);
                    //edt_area1.setText(subLocality);
                    state_tv.setText(state);
                    city_tv.setText(city);
                    areaLocation.setText(subLocality);
                }

                if (!isLocationAlreadySet) {
                    currentLocationGet();
                    isLocationAlreadySet = true;
                }


                getSelectNeighbourHoodList(true);
            } else {
                Log.d("Address", "No address found for the given coordinates.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Geocoder Error", "Unable to retrieve address.");
        }
    }

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(2000);
                locationRequest.setNumUpdates(1); // Only one update required

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mFusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location = locationResult.getLastLocation();
                                if (location != null) {
                                    Log.d("DEBUG_LOCATION", "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude() + ", Accuracy: " + location.getAccuracy());
                                    getAddress1(location.getLatitude(), location.getLongitude());
                                } else {
                                    Log.d("Location", "Failed to get fresh location");
                                    Toast.makeText(getApplicationContext(), "Unable to get current location.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        Looper.getMainLooper()
                );

            } else {
                // Location service disabled hai, dialog show karo
                showLocationPermissionDialog();
            }
        } else {
            // Permission nahi hai, dialog show karo
          //  showLocationPermissionDialog();
        }
    }


    private void showLocationPermissionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.location_permission_dialog);

        // Back press se band ho sakta hai, but outside touch se nahi
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        LinearLayout enableLocation = dialog.findViewById(R.id.btn_enable_location);
        LinearLayout enterManually = dialog.findViewById(R.id.btn_enter_location_manually);

        enableLocation.setOnClickListener(v -> {
            dialog.dismiss();
            if (!checkPermissions()) {
                requestPermissions();
            } else if (!isLocationEnabled()) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        enterManually.setOnClickListener(v -> {
            isManualLocationSelected = true;

            // Save preference so next time dialog won't appear
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(KEY_MANUAL_SELECTED, true);
            editor.apply();

            dialog.dismiss();
        });

        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) { // PERMISSION_ID = 44 hona chahiye
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // All permissions granted, location get karo
                //  Toast.makeText(this, "Location permissions granted!", Toast.LENGTH_SHORT).show();
                getLastLocation(); // Dobara call karo
            } else {
                // Permission denied
                boolean shouldShowRationale = false;
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        shouldShowRationale = true;
                        break;
                    }
                }

                if (shouldShowRationale) {
                    // User ne sirf deny kiya hai
                    Toast.makeText(this, "Location permissions are required for this feature.", Toast.LENGTH_SHORT).show();
                    // Phir se dialog show kar sakte hai ya kuch aur action le sakte hai
                } else {
                    // User ne "Don't Ask Again" select kiya hai
                    Toast.makeText(this, "Please enable location permission from Settings.", Toast.LENGTH_SHORT).show();
                    // Settings me bhejne ka option de sakte hai
                }
            }
        }
        // Baaki request codes ke liye existing code...
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onClick(String categoryName) {
        select_gender.setText(categoryName);
        mail_dialog.dismiss();
        edt_address1.requestFocus(); // Clears focus from the EditText
        edt_address1.setCursorVisible(false);
        edt_address2.requestFocus(); // Clears focus from the EditText
        edt_address2.setCursorVisible(false);
    }

    public void date1() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDateofBirth.setText(sdf.format(calendar.getTime()));
    }

    private void neighborhoodLocationStatusUnified(int mode) {
        if (!UtilityFunction.isNetworkConnected(context)) {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mode == 0) {
            UtilityFunction.showLoading(context, "Please wait...");
        }

        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userid", sm.getString("user_id"));
        hashMap.put("countryid", COUNTRY_ID);
       // hashMap.put("address", edt_area1.getText().toString());
        hashMap.put("area", edt_area1.getText().toString());
        Log.d("Sfsdfsdf",edt_area1.getText().toString());
        hashMap.put("lati", String.valueOf(latitude));
        hashMap.put("longi", String.valueOf(longitude));
        hashMap.put("stateid", state_tv.getText().toString());
        hashMap.put("cityid", city_tv.getText().toString());
        hashMap.put("pincode", edt_pincode.getText().toString());
        hashMap.put("status", SUCCESS_STATUS);
        ApiExecutor.getApiService().requestNeighbrhood("neighborhoodstatus", hashMap)
                .enqueue(new Callback<AddressResponse>() {
                    @Override
                    public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                        UtilityFunction.hideLoading();
                        try {
                            if (response.body() != null) {
                                String message = response.body().getMessage();
                                String cleanMessage = message.replace("\u00A0", " ").trim();
                                if (mode == 0) {
                                    if ("Thank you for your interest in Neighbrsnook. We aren’t in your city yet. We plan to be there soon!".equals(message)) {
                                        checkMissingStateDialog(message);
                                    } else if ("Thank you for your interest in Neighbrsnook. We are not there in your area yet and will let you know when we are.".equalsIgnoreCase(cleanMessage)) {
                                        checkMissingAreaDialog(message);
                                    }

                                } else if (mode == 1) {
                                    if ("Neighborhood found.".equals(message)) {
                                        // Handle neighborhood found case here if needed
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddressResponse> call, Throwable t) {
                        UtilityFunction.hideLoading();
                        // Handle failure
                    }
                });
    }
    private void currentLocationGet() {
        if (GlobalMethods.checkConnection(this)) {
            final HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", Integer.parseInt(sm.getString("user_id")));
            hashMap.put("country_name", "India");
            hashMap.put("latitude", String.valueOf(latitude));
            hashMap.put("longitude", String.valueOf(longitude));
            hashMap.put("area_name", st_area);
            hashMap.put("pincode", st_pincode);
            hashMap.put("state_name", st_state);
            hashMap.put("city_name", st_city);
            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<ResponseBody> call = service.neighLocationStatus(hashMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String resStr = response.body().string();
                        Log.d("RawResponse", resStr);

                        // Try parsing JSON if possible
                        if (resStr.trim().startsWith("{")) {
                            JSONObject jsonObject = new JSONObject(resStr);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if ("success".equals(status)) {
                                // Toast.makeText(AddressProof.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(AddressProof.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SecondPageUserLocationRegisteration.this, resStr, Toast.LENGTH_SHORT).show(); // handle plain string
                        }

                    } catch (Exception e) {
                        Log.e("ParseError", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SecondPageUserLocationRegisteration.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            GlobalMethods.getInstance(context).globalDialog(this, "No internet connection.");
        }
    }


    private void addressProofDocumentSubmit() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            HashMap<String, RequestBody> hashMap = new HashMap<>();
            hashMap.put("userid", RequestBody.create(MultipartBody.FORM, sm.getString("user_id")));
            hashMap.put("address", RequestBody.create(MultipartBody.FORM, edt_address1.getText().toString()));
            hashMap.put("pincode", RequestBody.create(MultipartBody.FORM, edt_pincode.getText().toString()));
            hashMap.put("areas", RequestBody.create(MultipartBody.FORM, String.valueOf(nbdId)));
            hashMap.put("lati", RequestBody.create(MultipartBody.FORM, String.valueOf(latitude)));
            hashMap.put("longi", RequestBody.create(MultipartBody.FORM, String.valueOf(longitude)));

            ApiExecutor.getApiService().addressProofPhoto("reg-step-II", null, null, hashMap)
                    .enqueue(new Callback<NeighbhoodAddressModel>() {
                        @Override
                        public void onResponse(Call<NeighbhoodAddressModel> call, Response<NeighbhoodAddressModel> response) {
                            UtilityFunction.hideLoading();
                            progressDialog.dismiss();

                            try {
                                NeighbhoodAddressModel body = response.body();
                                if (body != null && "success".equalsIgnoreCase(body.getStatus())) {

                                    String refMsg = body.getReferrerMsg();
                                    int refStatus = body.getReferrerNeighbourhoodStatus();

                                    // ✅ Always save refStatus (even if refMsg is null or empty)
                                    sm.setString("referrer_neighbourhood_status", String.valueOf(refStatus));
                                    Log.d("SAVE_STATUS", "Saved refStatus: " + refStatus + " | refMsg: " + refMsg);

                                    if (refMsg != null && !refMsg.trim().isEmpty()) {
                                        // ✅ Show dialog if message available
                                        showReferrerDialog(refMsg, refStatus);
                                    } else {
                                        // 🔹 Normal redirect if no message
                                        redirectAccordingToSource();
                                    }

                                } else if (body != null && body.getMessage() != null) {
                                    Toast.makeText(context, body.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<NeighbhoodAddressModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.e("addressProofError", t.toString());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }
    private void reachOutBtn() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            HashMap<String, String> hashMap = createRequestBodyMap(); // ab ye String map return karega

            ApiExecutor.getApiService().reachoutIIndStep(
                    "requestneighborhood",  // correct flag name
                    hashMap
            ).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    dialog.dismiss();
                    if (response.body() != null) {
                        String message = response.body().getMessage();
                        if ("success".equals(response.body().getStatus())) {
                            welcomeDialogReachout(message);
                        } else if (message != null) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Unexpected server response", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("Error", t.toString());
                    Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String> createRequestBodyMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userid", sm.getString("user_id"));
        hashMap.put("address", edt_address1.getText().toString());
        hashMap.put("countryid", "100");
        hashMap.put("stateid", state_tv.getText().toString());
        hashMap.put("cityid", city_tv.getText().toString());
        hashMap.put("pincode", edt_pincode.getText().toString());
        hashMap.put("area", edt_area1.getText().toString());
        hashMap.put("lati", String.valueOf(latitude));
        hashMap.put("longi", String.valueOf(longitude));

        Log.d("dsdsdsdde",edt_address1.getText().toString());
        Log.d("dsdsdsdde",state_tv.getText().toString());
        Log.d("dsdsdsdde",city_tv.getText().toString());
        Log.d("dsdsdsdde",edt_pincode.getText().toString());
        Log.d("dsdsdsdde",edt_area1.getText().toString());
        Log.d("dsdsdsdde",sm.getString("user_id"));
        return hashMap;
    }


    public void checkMissingStateDialog(String message) {
        final Dialog dialog = new Dialog(SecondPageUserLocationRegisteration.this);
        dialog.setContentView(R.layout.location_changer_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SecondPageUserLocationRegisteration.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        dialog.show();
        TextView frm_choose = dialog.findViewById(R.id.post_frm);
        frm_choose.setVisibility(VISIBLE);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        tvCancel.setVisibility(GONE);
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void checkMissingAreaDialog(String message) {
        final Dialog dialog = new Dialog(SecondPageUserLocationRegisteration.this);
        dialog.setContentView(R.layout.location_changer_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SecondPageUserLocationRegisteration.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        dialog.show();
        TextView frm_choose = dialog.findViewById(R.id.post_frm);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        frm_choose.setVisibility(VISIBLE);
        tvCancel.setVisibility(GONE);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    public void welcomeDialog() {
        final Dialog dialog = new Dialog(SecondPageUserLocationRegisteration.this);
        dialog.setContentView(R.layout.thanks_neighbrsnook_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SecondPageUserLocationRegisteration.this,
                android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView frmCancel = dialog.findViewById(R.id.tvCancel);
        TextView frm_choose = dialog.findViewById(R.id.post_frm);
        //littleMoreSkip1();
        TextView nghName = dialog.findViewById(R.id.nghName);
        TextView tvcancel = dialog.findViewById(R.id.tvCancel);
        nghName.setText(nghString);
        frmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondPageUserLocationRegisteration.this, LastPageUserDocumentRegisteration.class));
            }
        });
    }

    private boolean isEmpty(EditText editText, String errorMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            GlobalMethods.setError(editText, errorMessage);
            return true;
        }
        return false;
    }

    private void showToast(String message) {
        Toast.makeText(SecondPageUserLocationRegisteration.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean CheckAllFields() {
        // Check if areaName is empty or not
        String address1 = edt_address1.getText().toString();
        areaName = edt_area1.getText().toString().trim();
        if (areaName == null || areaName.isEmpty() || areaName.equals("")) {
            Toast.makeText(SecondPageUserLocationRegisteration.this, "Please select your residence area.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address1.isEmpty()) {
            edt_address1.setError(" Please enter your address");
            edt_address1.requestFocus();
            return false;
        } else if (BadWordFilter.containsBadWord(address1)) {
            GlobalMethods.getInstance(SecondPageUserLocationRegisteration.this).globalDialogAbusiveWord(SecondPageUserLocationRegisteration.this, getString(R.string.abusive_msg));
            return false;
        }

        return true;
    }

    private void stateApi(String id) {
        List<Integer> list = new ArrayList<>();
        list.add(Integer.valueOf(id));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.stateList("state", list);
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                try {
                    statelist = response.body().getNbdata();
                    for (Nbdatum nbdatum : statelist) {
                        if (nbdatum.getStateName().equals(state_tv.getText().toString())) {
                            stateid = Integer.parseInt(nbdatum.getId());
                            //  Log.d("slsfkskf", String.valueOf(stateid));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(SecondPageUserLocationRegisteration.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void cityApi(int id) {
        int stateId = id;
        List<Integer> list = new ArrayList<>();
        list.add(id);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.city("city", list);
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                citylist = response.body().getNbdata();
                for (Nbdatum nbdatum : citylist) {
                    if (nbdatum.getCity_name().equals(city_tv.getText().toString())) {
                        cityId = Integer.parseInt(nbdatum.getId());
                        Log.d("wwewrwe", String.valueOf(cityId));
                    }
                }
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(SecondPageUserLocationRegisteration.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    /*@Override
    public void onBackPressed() {
        //  neighborhoodLocationStatus();
        showBackConfirmationDialog(); // Call the common method to show the dialog
    }*/

    private void showBackConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Heads up!");
        builder.setMessage("If you go back now, you'll be logged out\n" +
                "and everything you've filled will be lost.\n" +
                "Want to continue?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String source = getIntent().getStringExtra("source");
                if ("register".equals(source)) {
                    // Agar login se aaya tha, toh LoginActivity open karo
                    Intent intent = new Intent(SecondPageUserLocationRegisteration.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Nahi toh normal back
                    onBackPressed();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // sirf dialog close
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void callDeviceInfoApi() {
        String deviceId = DeviceUtils.getDeviceId(SecondPageUserLocationRegisteration.this);
        //  String imei = DeviceUtils.getIMEI(requireContext());
        String modelName = DeviceUtils.getModelName();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("user_id", Integer.parseInt(sm.getString("user_id")));
        hm.put("device_id", deviceId);
        hm.put("device_model", modelName);
        // hm.put("device_imei", imei);
        hm.put("device_platform", "Android");

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<ReviewPojo> call = service.deviceInfoApi("deviceinfo", hm);
        call.enqueue(new Callback<ReviewPojo>() {
            @Override
            public void onResponse(Call<ReviewPojo> call, Response<ReviewPojo> response) {

                Log.d("res", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ReviewPojo> call, Throwable t) {
                Toast.makeText(context, "Device Id Api Error", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void logRegistrationEvent(String eventName, String status) {
        Bundle bundle = new Bundle();
        bundle.putString("status", status);  // Success, Failed, or Started
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }
    // Inside your activity, e.g., MainActivity.java


    public void init(){
        recy_selecy_neighbrhood = findViewById(R.id.select_neighbrhood_recy);
        location_child = findViewById(R.id.location_child);
        lytParent = findViewById(R.id.lytParent);
        lnrParentLocationNext = findViewById(R.id.lnrParentLocationNext);
        tvYourAddressProofNgh = findViewById(R.id.tvNghid);
        tvNghidAddress = findViewById(R.id.tvNghidAddress);
        frmVoterId = findViewById(R.id.frmVoterId);
        lnrFront = findViewById(R.id.lnrFront);
        tvNext = findViewById(R.id.tvNext);
        lnrBack = findViewById(R.id.lnrBack);
        genderFrm = findViewById(R.id.genderFrm);
        imgVoterFront = findViewById(R.id.imgVoterFront);
        imgVotertBack = findViewById(R.id.imgVotertBack);
        location_childChangelocation = findViewById(R.id.location_childChangelocation);
        frm_upload = findViewById(R.id.upload_id);
        imgAdharBack = findViewById(R.id.imgAdharBack);
        imgAdharFront = findViewById(R.id.imgAdharFront);
        adhar_front_img = findViewById(R.id.adhar_front_img);
        tvDateofBirth = findViewById(R.id.tv_date_of_birth);
        lnrNeighbrhoodUi = findViewById(R.id.lnrNeighbrhoodUi);
        adhar_back_img = findViewById(R.id.adhar_back_img);
        passport_front = findViewById(R.id.passport_front_img);
        passport_back = findViewById(R.id.passport_back_img);
        voter_front = findViewById(R.id.voter_front);
        frmDl = findViewById(R.id.frmDl);
        rentLease = findViewById(R.id.rentLease);
        voter_back = findViewById(R.id.voter_back);
        uload_reach_out = findViewById(R.id.uload_reach_out);
        edt_area1 = findViewById(R.id.edt_area);
        areaLocation = findViewById(R.id.locationIcon);
        useCurrentLocation = findViewById(R.id.useCurrentLocation);
        tvYourNeighbrhood = findViewById(R.id.tvYourNeighbrhood);
        addressTv = findViewById(R.id.address);
        country_tv = findViewById(R.id.country_tv);
        lnr_passport = findViewById(R.id.passport_id);
        lnr_voter_id = findViewById(R.id.voter_id);
        frm_aadhar_card = findViewById(R.id.adhar_card_id);
        frmPassport = findViewById(R.id.frmPassport);
        photo_dialog_open_rl = findViewById(R.id.upload_options_rl);
        img_back_address_proof = findViewById(R.id.img_address_proof_back);
        progressBar = findViewById(R.id.progerss_bar);
        state_tv = findViewById(R.id.state_address_proof);
        edt_address1 = findViewById(R.id.address_line_first_id);
        edt_address2 = findViewById(R.id.address_line_second_id);
        city_tv = findViewById(R.id.city_address_proof);
        imgDlFront = findViewById(R.id.imgDlFront);
        imgDltBack = findViewById(R.id.imgDltBack);
        tv = findViewById(R.id.id);
        currentLocationIconTv = findViewById(R.id.currentLocationIconTv);
        dobFrm = findViewById(R.id.dobFrm);
        edt_pincode = findViewById(R.id.pincode_id_address_proof);
        currentLocationIcon = findViewById(R.id.currentLocationIcon);
        changeLocationTv = findViewById(R.id.changeLocationTv);
        rdAdharCard = findViewById(R.id.rdAdharCard);
        rdPassport = findViewById(R.id.rdPassport);
        rdVoter = findViewById(R.id.rdVoter);
        rdDl = findViewById(R.id.rdDl);
        frmAdharCard = findViewById(R.id.frmAdharCard);
        imgPassportFront = findViewById(R.id.imgPassportFront);
        imgPassportBack = findViewById(R.id.imgPassportBack);
        yourNeighbrhood = findViewById(R.id.yourNeighbrhood);
        frmStepId = findViewById(R.id.frmStepId);
        select_gender = findViewById(R.id.select_gender_frm);

    }

    private void showReferrerDialog(String message, int refStatus) {
        final Dialog dialog = new Dialog(SecondPageUserLocationRegisteration.this);
        dialog.setContentView(R.layout.thanks_neighbrsnook_layout);

        // Set dialog window attributes
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(SecondPageUserLocationRegisteration.this, android.R.color.transparent))
        );
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);

        // Initialize views from custom layout
        TextView tvMessage = dialog.findViewById(R.id.tvMessage); // 👈 Add this TextView in your XML if not exist
        TextView tvOk = dialog.findViewById(R.id.post_frm);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        LinearLayout lnrAddressArea = dialog.findViewById(R.id.lnrAddressArea);
        lnrAddressArea.setVisibility(GONE);
        tvMessage.setVisibility(VISIBLE);
        tvOk.setText("OK");
        // Set the message text dynamically
        if (tvMessage != null) {
            tvMessage.setText(message);
        }

        // Cancel button listener
        tvCancel.setOnClickListener(v -> dialog.dismiss());

        // OK button listener (same functionality as old dialog)
        tvOk.setOnClickListener(v -> {
            sm.setString("referrer_neighbourhood_status", String.valueOf(refStatus));
            redirectAccordingToSource();
            dialog.dismiss();
        });

        dialog.show();
    }




    private void redirectAccordingToSource() {
        Intent intent;

        if ("profile".equalsIgnoreCase(source)) {
            intent = new Intent(SecondPageUserLocationRegisteration.this, ProfileUpdateDocumentUser.class);
            intent.putExtra("neighbrhood", locationNeighbrhood);
            intent.putExtra("stUploadDocument", stUploadDoc);
            intent.putExtra("source", "profile");

        } else if ("wall".equalsIgnoreCase(source)) {
            intent = new Intent(SecondPageUserLocationRegisteration.this, ProfileUpdateDocumentUser.class);
            intent.putExtra("stUploadDocument", stUploadDoc);
            intent.putExtra("source", "wall");

        } else {
            intent = new Intent(SecondPageUserLocationRegisteration.this, LastPageUserDocumentRegisteration.class);
            intent.putExtra("source", "unknown");
        }

        startActivity(intent);
    }

}
