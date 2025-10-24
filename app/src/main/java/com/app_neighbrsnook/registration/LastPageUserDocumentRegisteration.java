package com.app_neighbrsnook.registration;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.CountryAdapter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.RegistrationGenderAdapter;
import com.app_neighbrsnook.adapter.SelectionNeighbrhoodAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.StateDropdownPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.pojo.referal.Data;
import com.app_neighbrsnook.pojo.referal.ReferralResponse;
import com.app_neighbrsnook.utils.FileUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.MetaEventLogger;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastPageUserDocumentRegisteration extends AppCompatActivity implements View.OnClickListener, RegistrationGenderAdapter.CategoryInterface1, CountryAdapter.OnItemSelected, ImageUploadAdapter.ImageRequest, LocationListener {

    FrameLayout frm_upload, uload_reach_out, frmDl;
    ImageView img_back_address_proof;
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
    EditText edt_address1, edt_address2, edt_pincode;
    CountryAdapter countryAdapter;
    TextView country_tv, city_tv, state_tv, currentLocationIconTv;
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
    private Context mContext = LastPageUserDocumentRegisteration.this;
    EditText edt_area1;
    String nbdNamearea = "";
    FrameLayout frmStepId, genderFrm, dobFrm;
    private static final int REQUEST = 112;
    ArrayList<StateDropdownPojo> selectNeighbrhood = new ArrayList<>();
    TextView tv, changeLocationTv, select_gender, tvDateofBirth,frontid;
    String value;
    LinearLayout location_child, lnrNeighbrhoodUi, location_childChangelocation;
    ImageView imgAdharFront, imgAdharBack;
    String FIRST_IMAGE = "FirstImage";
    String SECOND_IMAGE = "SecondImage";
    String CLICK_ON = "";
    private static final int PIC_CROP_REQUEST = 2;
    TextView tv_take_photo, tv_choose_photo, tv_cancel;
    String nghString;
    HashMap<String, Object> hashMap;LinearLayout lytParent, lnrParentLocationNext;
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
    LinearLayout rentLeaseLnr,driverLicenseLnr,voterIdLnr,passportLnr,adharcardLnr,frmAdharCard;

    private LinearLayout[] allSelectableLayouts;
    String selectedIdType = "";
    TextView addressProofNghbrhod;
    private boolean shouldShowWelcomeDialog = true;
    CardView lnrBack, lnrFront,doc_section;
    ImageView doc_arrow_section,imgIconPrivacy;
    private boolean isExpanded = true;
    String phoneNumber,usernameRefered,referNeighbrhoodName;
    TextView tvReferalSet,textStatusDoc,textStatusNoDocs,tvSelectAddressText,tvOptional;
    FrameLayout referedDropdown,frmReferalUi;
    LinearLayout referalTextVisible;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        sm = new SharedPrefsManager(this);
        calendar = Calendar.getInstance();
        setContentView(R.layout.activity_address_document_demo);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // <- yeh yahan add karein
        frontid = findViewById(R.id.frontid);

        PrefMananger.saveScreen(context, PrefMananger.ADDRESS_PROOF);
        phoneNumber = sm.getString("phone_no");

        recy_selecy_neighbrhood = findViewById(R.id.select_neighbrhood_recy);
        imgIconPrivacy = findViewById(R.id.imgIconPrivacy);
        frmReferalUi = findViewById(R.id.frmReferalUi);
        tvOptional = findViewById(R.id.tvOptional);
        location_child = findViewById(R.id.location_child);
        lytParent = findViewById(R.id.lytParent);
        lnrParentLocationNext = findViewById(R.id.lnrParentLocationNext);
        tvYourAddressProofNgh = findViewById(R.id.tvNghid);
        tvNghidAddress = findViewById(R.id.tvNghidAddress);
        lnrFront = findViewById(R.id.lnrFront);
        lnrBack = findViewById(R.id.lnrBack);
        textStatusDoc = findViewById(R.id.textStatusDoc);
        textStatusNoDocs = findViewById(R.id.textStatusNoDocs);
        genderFrm = findViewById(R.id.genderFrm);
        location_childChangelocation = findViewById(R.id.location_childChangelocation);
        frm_upload = findViewById(R.id.upload_id);
        imgAdharBack = findViewById(R.id.imgAdharBack);
        imgAdharFront = findViewById(R.id.imgAdharFront);
        doc_arrow_section = findViewById(R.id.doc_arrow_section);
        tvReferalSet = findViewById(R.id.tvReferalSet);
        tvSelectAddressText = findViewById(R.id.tvSelectAddressText);
        doc_section = findViewById(R.id.doc_section);
        adhar_front_img = findViewById(R.id.adhar_front_img);
        tvDateofBirth = findViewById(R.id.tv_date_of_birth);
        lnrNeighbrhoodUi = findViewById(R.id.lnrNeighbrhoodUi);
        adhar_back_img = findViewById(R.id.adhar_back_img);
        tv_cancel = findViewById(R.id.cancle_tv);
        tv_take_photo = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
        passport_front = findViewById(R.id.passport_front_img);
        passport_back = findViewById(R.id.passport_back_img);
        voter_front = findViewById(R.id.voter_front);
        frmDl = findViewById(R.id.frmDl);
        rentLease = findViewById(R.id.rentLease);
        rentLeaseLnr = findViewById(R.id.rentLeaseLnr);
        driverLicenseLnr = findViewById(R.id.driverLicencseLnr);
        passportLnr = findViewById(R.id.passportLnr);
        voterIdLnr = findViewById(R.id.voterIdLnr);
        voter_back = findViewById(R.id.voter_back);
        uload_reach_out = findViewById(R.id.uload_reach_out);
        edt_area1 = findViewById(R.id.edt_area);
        areaLocation = findViewById(R.id.locationIcon);
        useCurrentLocation = findViewById(R.id.useCurrentLocation);
        yourNeighbrhood = findViewById(R.id.yourNeighbrhood);
        tvYourNeighbrhood = findViewById(R.id.tvYourNeighbrhood);
        addressTv = findViewById(R.id.address);
        country_tv = findViewById(R.id.country_tv);
        lnr_passport = findViewById(R.id.passport_id);
        lnr_voter_id = findViewById(R.id.voter_id);
        frm_aadhar_card = findViewById(R.id.adhar_card_id);
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
        yourNeighbrhood = findViewById(R.id.yourNeighbrhood);
        frmStepId = findViewById(R.id.frmStepId);
        adharcardLnr = findViewById(R.id.adharcardLnr);
        select_gender = findViewById(R.id.select_gender_frm);
        addressProofNghbrhod = findViewById(R.id.addressProofNghbrhod);
        referedDropdown = findViewById(R.id.referedDropdown);
        referalTextVisible = findViewById(R.id.referalTextVisible);
        //  nbdNamearea = String.valueOf(edt_area1);
        //    stateApi("100");
        state_tv.setOnClickListener(this);
        city_tv.setOnClickListener(this);
        frmStepId.setVisibility(GONE);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //callDeviceInfoApi();
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "manual_debug");
        FirebaseAnalytics.getInstance(this).logEvent("test_event", bundle);
        String refStatus = sm.getString("referrer_neighbourhood_status");

        Log.d("LOAD_STATUS", "Loaded refStatus: " + refStatus);
//        int referralStatus = sm.getInt("referral_status", 0); // default 0



        select_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGender();
            }
        });

        frm_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("1".equals(refStatus)) {
                    // ✅ Only validate gender & dob if referral is active
                    if (validateGenderAndDob()) {
                        addressProofDocumentSubmit();
                    }
                } else {
                    // ✅ Run full validation
                    if (CheckAllFields()) {
                        addressProofDocumentSubmit();
                    }
                }
            }
        });
        doc_arrow_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSection();
            }
        });
        img_back_address_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackConfirmationDialog();
               // onBackPressed();

            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
                photo_dialog_open_rl.setVisibility(GONE);
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                photo_dialog_open_rl.setVisibility(GONE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo_dialog_open_rl.setVisibility(GONE);
            }
        });
        shouldShowWelcomeDialog = false;
        littleMoreSkip1();
        getReferralDetails(phoneNumber);
        allSelectableLayouts = new LinearLayout[]{
                rentLeaseLnr, passportLnr, voterIdLnr, driverLicenseLnr, adharcardLnr
        };
        imgAdharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageDebug", "Front image clicked!");
                if (GlobalMethods.checkCameraAndGalleryPermission(LastPageUserDocumentRegisteration.this)) {
                    if (imgAdharFront.getDrawable() == null) {
                        Log.d("ImageDebug", "Front image drawable is null, opening camera/gallery");
                        CLICK_ON = FIRST_IMAGE;
                        photo_dialog_open_rl.setVisibility(VISIBLE);
                    } else {
                        type = 1;
                        Log.d("ImageDebug", "Front image has drawable, bitmap1 is null: " + (bitmap1 == null));

                        // Always try to get the current bitmap from ImageView
                        Drawable drawable = imgAdharFront.getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            Bitmap currentBitmap = ((BitmapDrawable) drawable).getBitmap();
                            if (currentBitmap != null) {
                                Log.d("ImageDebug", "Got current bitmap from ImageView for front");
                                // Update bitmap1 with current bitmap
                                bitmap1 = currentBitmap;
                                imageDialog(bitmap1);
                            } else {
                                Log.e("ImageDebug", "Current bitmap from ImageView is null");
                                Toast.makeText(LastPageUserDocumentRegisteration.this, "Image not available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("ImageDebug", "Drawable is not BitmapDrawable");
                            Toast.makeText(LastPageUserDocumentRegisteration.this, "Image not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        imgAdharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageDebug", "Back image clicked!");
                if (GlobalMethods.checkCameraAndGalleryPermission(LastPageUserDocumentRegisteration.this)) {
                    if (imgAdharBack.getDrawable() == null) {
                        Log.d("ImageDebug", "Back image drawable is null, opening camera/gallery");
                        CLICK_ON = SECOND_IMAGE;
                        photo_dialog_open_rl.setVisibility(VISIBLE);
                    } else {
                        type = 2;
                        Log.d("ImageDebug", "Back image has drawable, bitmap2 is null: " + (bitmap2 == null));

                        // Always try to get the current bitmap from ImageView
                        Drawable drawable = imgAdharBack.getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            Bitmap currentBitmap = ((BitmapDrawable) drawable).getBitmap();
                            if (currentBitmap != null) {
                                Log.d("ImageDebug", "Got current bitmap from ImageView for back");
                                // Update bitmap2 with current bitmap
                                bitmap2 = currentBitmap;
                                imageDialog(bitmap2);
                            } else {
                                Log.e("ImageDebug", "Current bitmap from ImageView is null");
                                Toast.makeText(LastPageUserDocumentRegisteration.this, "Image not available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("ImageDebug", "Drawable is not BitmapDrawable");
                            Toast.makeText(LastPageUserDocumentRegisteration.this, "Image not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        rentLeaseLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                rentLeaseLnr.setBackgroundResource(R.drawable.card_select_document);
                rentLeaseLnr.setSelected(true);
                // Reset images
                imgAdharFront.setImageDrawable(null);
                imgAdharBack.setImageDrawable(null);

                // Show only front
                frmAdharCard.setVisibility(VISIBLE);
                lnrFront.setVisibility(VISIBLE);
                lnrBack.setVisibility(GONE);

                // Label change only for rent lease
                frontid.setText("Upload Rent Lease");
            }
        });
        passportLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                passportLnr.setBackgroundResource(R.drawable.card_select_document);
                passportLnr.setSelected(true);
                bitmap1 = null;
                bitmap2 = null;
                imgAdharFront.setImageDrawable(null);
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(VISIBLE);
                lnrFront.setVisibility(VISIBLE);
                lnrBack.setVisibility(VISIBLE);
                frontid.setText("Front");

            }
        });
        voterIdLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                voterIdLnr.setBackgroundResource(R.drawable.card_select_document);
                voterIdLnr.setSelected(true);
                bitmap1 = null;
                bitmap2 = null;
                imgAdharFront.setImageDrawable(null);
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(VISIBLE);
                lnrFront.setVisibility(VISIBLE);
                lnrBack.setVisibility(VISIBLE);
                frontid.setText("Front");

            }
        });
        driverLicenseLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                driverLicenseLnr.setBackgroundResource(R.drawable.card_select_document);
                driverLicenseLnr.setSelected(true);
                imgAdharFront.setImageDrawable(null);
                bitmap1 = null;
                bitmap2 = null;
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(VISIBLE);
                lnrFront.setVisibility(VISIBLE);
                lnrBack.setVisibility(VISIBLE);
                frontid.setText("Front");

            }
        });
        adharcardLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                adharcardLnr.setBackgroundResource(R.drawable.card_select_document);
                adharcardLnr.setSelected(true);
                imgAdharFront.setImageDrawable(null);
                bitmap1 = null;
                bitmap2 = null;
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(VISIBLE);
                lnrFront.setVisibility(VISIBLE);
                lnrBack.setVisibility(VISIBLE);
                frontid.setText("Front");

            }
        });
        if ("1".equals(refStatus)) {
            // No need for document in this scenario
            doc_section.setVisibility(GONE);
            referedDropdown.setVisibility(VISIBLE);
            textStatusDoc.setVisibility(GONE);
            imgIconPrivacy.setVisibility(GONE);
            tvSelectAddressText.setVisibility(VISIBLE);
            frmReferalUi.setVisibility(VISIBLE);
            tvOptional.setVisibility(VISIBLE);
            Log.d("ReferralStatus", "Referred user - document optional");
        } else {
            // Default case: document upload required
            tvSelectAddressText.setVisibility(VISIBLE);
            doc_section.setVisibility(VISIBLE);
            imgIconPrivacy.setVisibility(VISIBLE);
            doc_arrow_section.setVisibility(GONE);
            frmReferalUi.setVisibility(GONE);
            referedDropdown.setVisibility(VISIBLE);
            textStatusDoc.setVisibility(VISIBLE);
            textStatusNoDocs.setVisibility(GONE);
            tvOptional.setVisibility(GONE);
            Log.d("ReferralStatus", "No Referral - document required");
        }
        DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                date1(); // Your custom method to update UI or handle the selected date
            }
        };

        tvDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                maxDate.add(Calendar.YEAR, -13); // Today minus 13 years

                DatePickerDialog pickerDialog = new DatePickerDialog(LastPageUserDocumentRegisteration.this, fromdate,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                pickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis()); // Max date: 13 years ago

                // Optional: If you want to set a minimum DOB (e.g., 100 years ago max age)
                Calendar minDate = Calendar.getInstance();
                minDate.add(Calendar.YEAR, -100); // Optional minimum age = 100 years
                pickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

                pickerDialog.show();

                edt_address2.clearFocus();
                edt_address2.setCursorVisible(false);
                edt_address1.clearFocus();
                edt_address1.setCursorVisible(false);
            }
        });


        dobFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(LastPageUserDocumentRegisteration.this, fromdate, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                pickerDialog.show();
            }
        });
        genderFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGender();
                edt_address1.clearFocus(); // Clears focus from the EditText
                edt_address1.setCursorVisible(false);
            }
        });
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 13);
        frm_upload.setVisibility(VISIBLE);
        country_tv.setText("India");
        // getAddress(latitude,longitude );
        //29-05-25    requestAppPermissions();
        //handleLocationPermission();
       // getLastLocation();

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
                   // stateApi(id);
                    break;

                case "state":
                    state_tv.setText(name);
                    stateName = name;
                    stateid = parsedId;
                   // cityApi(parsedId);
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
        final Dialog dialog = new Dialog(LastPageUserDocumentRegisteration.this);
        dialog.setContentView(R.layout.reachout_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(LastPageUserDocumentRegisteration.this,
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
                startActivity(new Intent(LastPageUserDocumentRegisteration.this, LoginActivity.class));
                finish();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK && data != null) {

                /*    String placeName = data.getStringExtra("place_name");
                    String placeAddress = data.getStringExtra("place_address");
                    double latitude = data.getDoubleExtra("latitude", 0.0);
                    double longitude = data.getDoubleExtra("longitude", 0.0);
                    if (placeName != null) areaLocation.setText(placeName);
                    if (placeAddress != null) addressTv.setText(placeAddress);
                    Log.d("sdsdfsdfae", areaLocation.getText().toString());*/
                   // getAddress(latitude, longitude);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
                  /*  Status status = Autocomplete.getStatusFromIntent(data);
                    if (status != null && status.getStatusMessage() != null) {
                        Log.i("TAG_status", status.getStatusMessage());
                    }*/

                }
            }
            if (requestCode == 1) {
                // bitmap = (Bitmap) data.getExtras().get("data");
                Log.e("1258963", bitmap + "");
                File file1 = bitmapToFile(bitmap, getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg");
                //filePath = Uri.fromFile(file1);
                filePath = imageUri;
                bitmap1 = bitmap;
                performCrop(filePath);

            } else if (requestCode == PIC_CROP_REQUEST && data != null) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                performCrop(filePath);
                if (CLICK_ON.equals(FIRST_IMAGE)) {
                    bitmap1 = bitmap;
                    filePath1 = filePath;
                    imgAdharFront.setVisibility(VISIBLE);
                    imgAdharFront.setImageBitmap(bitmap);
                    imgAdharFront.setVisibility(VISIBLE);
                    ImagePOJO imagePOJO = new ImagePOJO();
                    imagePOJO.bitmap = bitmap;
                    imagePOJO.imageUri = filePath;
                } else if (CLICK_ON.equals(SECOND_IMAGE)) {
                    bitmap2 = bitmap;
                    filePath2 = filePath;
                    imgAdharBack.setImageBitmap(bitmap);
                    imgAdharBack.setVisibility(VISIBLE);
                    imgAdharBack.setVisibility(VISIBLE);
                    //  runTextRecognition(imageUri);

                }
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo1;
                    try {
                        photo1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        byte[] byteArrayImage = bytes.toByteArray();
                        filePath = resultUri;
                        bitmap = photo1;

                        if (CLICK_ON.equals(FIRST_IMAGE)) {
                            Log.d("ImageDebug", "Processing FIRST_IMAGE");
                            bitmap1 = bitmap; // Set original bitmap first
                            filePath1 = filePath;

                            try {
                                InputImage image = InputImage.fromFilePath(this, resultUri);
                                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                                recognizer.process(image)
                                        .addOnSuccessListener(visionText -> {
                                            Log.d("ImageDebug", "Text recognition success for front image");

                                            // Call blur method and get result
                                            Bitmap blurredBitmap = blurAadhaarNumber(resultUri, visionText);

                                            if (blurredBitmap != null) {
                                                Log.d("ImageDebug", "Blurred bitmap created successfully for front");

                                                // IMPORTANT: Update bitmap1 with blurred version
                                                bitmap1 = blurredBitmap;

                                                // Update UI on main thread
                                                runOnUiThread(() -> {
                                                    imgAdharFront.setImageBitmap(bitmap1);
                                                    Log.d("ImageDebug", "Front image set to ImageView");
                                                });

                                                // Create file from blurred bitmap in internal cache
                                                try {
                                                    File file = new File(getCacheDir(), "blurred_front_" + System.currentTimeMillis() + ".jpg");
                                                    FileOutputStream out = new FileOutputStream(file);
                                                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                                    out.flush();
                                                    out.close();
                                                    filePath1 = Uri.fromFile(file);
                                                    Log.d("ImageDebug", "File created: " + file.getAbsolutePath());
                                                } catch (Exception e) {
                                                    Log.e("ImageDebug", "Error creating file for front image", e);
                                                }
                                            } else {
                                                Log.e("ImageDebug", "Blurred bitmap is null for front image");
                                                Toast.makeText(this, "Failed to process front image", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("ImageDebug", "Text recognition failed for front: " + e.getMessage());
                                            Toast.makeText(this, "Text recognition failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });

                            } catch (IOException e) {
                                Log.e("ImageDebug", "IOException in front image processing", e);
                                e.printStackTrace();
                            }
                        }
                        else if (CLICK_ON.equals(SECOND_IMAGE)) {
                            Log.d("ImageDebug", "Processing SECOND_IMAGE");
                            bitmap2 = bitmap; // Set original bitmap first
                            filePath2 = filePath;

                            try {
                                InputImage image = InputImage.fromFilePath(this, resultUri);
                                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                                recognizer.process(image)
                                        .addOnSuccessListener(visionText -> {
                                            Log.d("ImageDebug", "Text recognition success for back image");

                                            // Call blur method and get result
                                            Bitmap blurredBitmap = blurAadhaarNumber(resultUri, visionText);

                                            if (blurredBitmap != null) {
                                                Log.d("ImageDebug", "Blurred bitmap created successfully for back");

                                                // IMPORTANT: Update bitmap2 with blurred version
                                                bitmap2 = blurredBitmap;

                                                // Update UI on main thread
                                                runOnUiThread(() -> {
                                                    imgAdharBack.setImageBitmap(bitmap2);
                                                    Log.d("ImageDebug", "Back image set to ImageView");
                                                });

                                                // Create file from blurred bitmap in internal cache
                                                try {
                                                    File file = new File(getCacheDir(), "blurred_back_" + System.currentTimeMillis() + ".jpg");
                                                    FileOutputStream out = new FileOutputStream(file);
                                                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                                    out.flush();
                                                    out.close();
                                                    filePath2 = Uri.fromFile(file);
                                                    Log.d("ImageDebug", "File created: " + file.getAbsolutePath());
                                                } catch (Exception e) {
                                                    Log.e("ImageDebug", "Error creating file for back image", e);
                                                }
                                            } else {
                                                Log.e("ImageDebug", "Blurred bitmap is null for back image");
                                                Toast.makeText(this, "Failed to process back image", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("ImageDebug", "Text recognition failed for back: " + e.getMessage());
                                            Toast.makeText(this, "Text recognition failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });

                            } catch (IOException e) {
                                Log.e("ImageDebug", "IOException in back image processing", e);
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        Log.e("ImageDebug", "General exception in onActivityResult", e);
                        e.printStackTrace();
                    }
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.e("ImageDebug", "Crop error", error);
                    error.printStackTrace();
                }
            }


            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //*@Override
    public void removeImage(int position) {
        bitmapList.remove(position);
        uploadAdapter.notifyDataSetChanged();
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
    // Activity start hone par check karo
    @Override
    protected void onStart() {
        super.onStart();
        // Agar permission nahi hai ya location off hai, dialog show karo
      /*  if (!checkPermissions() || !isLocationEnabled()) {
            showLocationPermissionDialog();
        }*/
    }

    // Resume par bhi check kar sakte hai (optional)
    @Override
    public void onResume() {
        super.onResume();
        // Agar chahte hai ki resume par bhi check ho
        // if (!checkPermissions() || !isLocationEnabled()) {
        //     showLocationPermissionDialog();
        // }
    }

    private void selectGender() {
        RecyclerView rv;
        CardView cardView;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.selct_genderr);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cardView = mail_dialog.findViewById(R.id.cv_cancel);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RegistrationGenderAdapter emailListAdapter = new RegistrationGenderAdapter(mail_dialog, LastPageUserDocumentRegisteration.this);
        rv.setAdapter(emailListAdapter);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();

            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(LastPageUserDocumentRegisteration.this, android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();/*
        FrameLayout frmpost=mail_dialog.findViewById(R.id.post_frm);
        frmpost.setVisibility(View.GONE);*/
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
    Bitmap bitmap;
    Uri filePath;
    Bitmap bitmap1;
    Bitmap bitmap2;
    Uri filePath1;
    Uri filePath2;
    String currentPath = "";
    Uri imageUri;
    public void capturePhoto() {
        String fileName = System.currentTimeMillis() + "";
        File fileDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(fileName, ".jpg", fileDirectory);
            currentPath = imageFile.getAbsolutePath();
            imageUri = FileProvider.getUriForFile(this, "com.app_neighbrsnook.fileprovider", imageFile);
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePicture, 1);//zero can be replaced with any action code (called
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void performCrop(Uri fileUri) {
        try {
            CropImage.ActivityBuilder a1 = CropImage.activity(fileUri);
            a1.setCropMenuCropButtonTitle("Set Image");
            a1.setAspectRatio(1, 1);
            a1.setFixAspectRatio(false);

            a1.start(activity);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void addressProofDocumentSubmit() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            File file1;
            int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress
            MultipartBody.Part aadharFront = null;
            String frontkey = "";
            String backKey = "";
            if (adharcardLnr.isSelected()) {
                frontkey = "aadharFront";
                backKey = "aadharBack";
            } else if (passportLnr.isSelected()) {
                frontkey = "passportFront";
                backKey = "passportBack";
            } else if (voterIdLnr.isSelected()) {
                frontkey = "voterFront";
                backKey = "voterBack";
            } else if (driverLicenseLnr.isSelected()) {
                frontkey = "dlFront";
                backKey = "dlBack";
            } else if (rentLeaseLnr.isSelected()) {
                frontkey = "rentdocs";
            }

            if (filePath1 != null) {
                file1 = FileUtil.from(this, filePath1);
                try {
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }
                RequestBody videoPart = null;
                try {
                    Bitmap bmp = BitmapFactory.decodeFile(file1.getAbsolutePath());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                    Log.e("imageSize", bos.toByteArray().length + " Byte \n" + (bos.toByteArray().length / 1024) + "KB");
                    videoPart = RequestBody.create(MediaType.parse(getContentResolver().getType(filePath2)), bos.toByteArray());
                    Log.e("Name", file1.getName());
                    Log.e("Type", file1.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    videoPart = RequestBody.create(MediaType.parse(".jpg"), file1);
                }
                aadharFront = MultipartBody.Part.createFormData(frontkey, file1.getName(), videoPart);
            } else if (bitmap1 != null) {
                file1 = bitmapToFile(bitmap1, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(file1.getPath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file1));
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t);
                    t.printStackTrace();
                }
                RequestBody videoPart = RequestBody.create(MediaType.parse(".jpg"), file1);
                if (file1.getName() == null) {
                } else {
                    aadharFront = MultipartBody.Part.createFormData(frontkey, file1.getName(), videoPart);
                }
            }

            File file2;
            MultipartBody.Part aadharBack = null;
            if (filePath2 != null) {
                file2 = FileUtil.from(this, filePath2);
                try {
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }
                RequestBody videoPart = null;
                try {
                    Bitmap bmp = BitmapFactory.decodeFile(file2.getAbsolutePath());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                    Log.e("imageSize", bos.toByteArray().length + " Byte \n" + (bos.toByteArray().length / 1024) + "KB");
                    videoPart = RequestBody.create(MediaType.parse(getContentResolver().getType(filePath2)), bos.toByteArray());
                    Log.e("Name", file2.getName());
                    Log.e("Type", file2.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    videoPart = RequestBody.create(MediaType.parse(".jpg"), file2);
                }
                aadharBack = MultipartBody.Part.createFormData(backKey, file2.getName(), videoPart);
            } else if (bitmap2 != null) {
                file2 = bitmapToFile(bitmap2, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(file2.getPath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file2));
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t);
                    t.printStackTrace();
                }
                RequestBody videoPart = RequestBody.create(MediaType.parse(".jpg"), file2);
                if (file2.getName() == null) {
                } else {
                    aadharBack = MultipartBody.Part.createFormData(backKey, file2.getName(), videoPart);
                }
            }

            HashMap<String, RequestBody> hashMap = new HashMap<>();
            hashMap.put("userid", RequestBody.create(MultipartBody.FORM, sm.getString("user_id")));
            hashMap.put("dob", RequestBody.create(MultipartBody.FORM, tvDateofBirth.getText().toString()));
            hashMap.put("referral_code", RequestBody.create(MultipartBody.FORM, tvReferalSet.getText().toString()));

            //Log.d("erwerwsf", String.valueOf(RequestBody.create(MultipartBody.FORM, String.valueOf(cityId))));
            if (select_gender.getText().toString().equals("Male")) {
                hashMap.put("gender", RequestBody.create(MultipartBody.FORM, "1"));
            } else if (select_gender.getText().toString().equals("Female")) {
                hashMap.put("gender", RequestBody.create(MultipartBody.FORM, "2"));
            } else if (select_gender.getText().toString().equals("Other")) {
                hashMap.put("gender", RequestBody.create(MultipartBody.FORM, "3"));
            }

            ApiExecutor.getApiService().addressProofPhotoLast("reg-step-III", aadharFront, aadharBack, hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        if (response.body().getStatus().equals("success")) {
                            dialog.dismiss();
                            logRegistrationEvent("registration_completed_android", "user_signup_done_android");
                            String userId = sm.getString("user_id");
                            shouldShowWelcomeDialog = true;
                            littleMoreSkip1();
                        } else if (response.body().getMessage() != null) {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("fdsadf", t.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }


    private void getReferralDetails(String phoneNumber) {
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);

        Call<ReferralResponse> call = service.getUserReferralByPhone(phoneNumber, "DEV-3a9f1d2e7b8c4d6f1234abcd5678ef90");

        call.enqueue(new Callback<ReferralResponse>() {
            @Override
            public void onResponse(Call<ReferralResponse> call, Response<ReferralResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ReferralResponse referralResponse = response.body();
                    Data data = referralResponse.getData();

                    // Example: Showing the retrieved info in log
                    Log.d("ReferralAPI", "Referral Name: " + data.getReferred_name());
                    Log.d("ReferralAPI", "Referral Code: " + data.getReferral_code());
                    Log.d("ReferralAPI", "Referrer Name: " + data.getReferrer().getName());
                    Log.d("ReferralAPI", "Referrer Phone: " + data.getReferrer().getPhoneno());


                    // You can also display it in TextView
                    tvReferalSet.setText(data.getReferral_code());
                    usernameRefered=data.getReferrer().getName();
                    referNeighbrhoodName=data.getReferrer().getNbd_name();
                    String fullText = "You have been referred by " + usernameRefered +
                            " from " + referNeighbrhoodName +
                            ", hence providing ID is optional";

                    SpannableString spannable = new SpannableString(fullText);
                    int start = fullText.indexOf(usernameRefered);
                    int end = fullText.indexOf(referNeighbrhoodName) + referNeighbrhoodName.length();
                    spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// set to TextView
                    textStatusNoDocs.setText(spannable);
                    // textReferralCode.setText(data.getReferral_code());
                } else {
                    Log.e("ReferralAPI", "Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ReferralResponse> call, Throwable t) {
                Log.e("ReferralAPI", "Error: " + t.getMessage());
            }
        });
    }

    private void imageDialog(Bitmap bitmap) {
        RecyclerView rv;
        TextView confirm;
        ImageView cancel, iv_uploaded_image, iv_remove;
        CardView card;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.address_proof_dialog_layout);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        iv_remove = image_dialog.findViewById(R.id.iv_remove_image);
        iv_uploaded_image = image_dialog.findViewById(R.id.iv_uploaded_image);

        // Set the bitmap to dialog ImageView
        iv_uploaded_image.setImageBitmap(bitmap);

        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    bitmap1 = null;
                    iv_uploaded_image.setImageDrawable(null);
                    imgAdharFront.setImageDrawable(null);
                    imgAdharFront.setVisibility(VISIBLE);
                } else if (type == 2) {
                    bitmap2 = null;
                    iv_uploaded_image.setImageDrawable(null);
                    imgAdharBack.setImageDrawable(null);
                    imgAdharBack.setVisibility(VISIBLE);
                }
                image_dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(LastPageUserDocumentRegisteration.this, android.R.color.white)));
        image_dialog.setCancelable(true);
        image_dialog.show();
    }

    private void littleMoreSkip1() {
        hashMap = new HashMap<>();
        hashMap.put("userid", sm.getString("user_id"));
        userProfile1(hashMap);
    }
    private void userProfile1(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(sm.getString("user_id")), hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement = response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                try {
                    sm.setString("userphoto", jsonObject.get("userpic").getAsString());
                    nghString = jsonObject.get("neighborhood").getAsString();
                  //  addressProofNghbrhod.setText("Id &amp; Address Proof "+ " "+ "("+ nghString+ ")");
                    addressProofNghbrhod.setText("(for " + nghString + ")");

                    if (shouldShowWelcomeDialog) {
                        welcomeDialog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(LastPageUserDocumentRegisteration.this, "Data not found", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }
    public void welcomeDialog() {
        final Dialog dialog = new Dialog(LastPageUserDocumentRegisteration.this);
        dialog.setContentView(R.layout.thanks_neighbrsnook_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(LastPageUserDocumentRegisteration.this, android.R.color.transparent))
        );
        dialog.getWindow().setAttributes(lp);

        TextView frmCancel = dialog.findViewById(R.id.tvCancel);
        TextView frm_choose = dialog.findViewById(R.id.post_frm);
        TextView nghName = dialog.findViewById(R.id.nghName);
        TextView tvcancel = dialog.findViewById(R.id.tvCancel);

        nghName.setText(nghString);

        frmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 🔹 Get referralStatus from SharedPreferences (PrefManager)
        int referralStatus = sm.getInt("referral_status", 0); // default 0

        // 🔹 Condition check
        if (referralStatus == 1) {
            // 👉 Directly go to MainActivity
            frm_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LastPageUserDocumentRegisteration.this, MainActivity.class));
                    finishAffinity();
                }
            });
        } else {
            // 👉 Continue with normal flow
            frm_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    welcomeDialogLast();
                }
            });
        }

        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(LastPageUserDocumentRegisteration.this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean CheckAllFields() {
        if (adharcardLnr.isSelected()) {
            selectedIdType = "Aadhaar";

            if (bitmap1 == null) {
                showToast("Please upload the front of your Aadhaar.");
                return false;
            }
            if (bitmap2 == null) {
                showToast("Please upload the back of your Aadhaar.");
                return false;
            }

        } else if (passportLnr.isSelected()) {
            selectedIdType = "Passport";

            if (bitmap1 == null) {
                showToast("Please upload the photo page of your Passport.");
                return false;
            }
            if (bitmap2 == null) {
                showToast("Please upload the address page of your Passport.");
                return false;
            }

        } else if (voterIdLnr.isSelected()) {
            selectedIdType = "Voter ID";

            if (bitmap1 == null) {
                showToast("Please upload the front of your Voter ID.");
                return false;
            }
            if (bitmap2 == null) {
                showToast("Please upload the back of your Voter ID.");
                return false;
            }

        } else if (driverLicenseLnr.isSelected()) {
            selectedIdType = "Driving License";

            if (bitmap1 == null) {
                showToast("Please upload the front of your Driving License.");
                return false;
            }
            if (bitmap2 == null) {
                showToast("Please upload the back of your Driving License.");
                return false;
            }

        } else if (rentLeaseLnr.isSelected()) {
            selectedIdType = "Rent Lease";

            if (bitmap1 == null) {
                showToast("Please upload your Rental Lease.");
                return false;
            }

        } else {
            showToast("Please select a document type for your address proof.");
            return false;
        }

        // gender validation
        if (select_gender.getText().toString().trim().isEmpty()) {
            showToast("Please select gender");
            return false;
        }

        // dob validation
        if (tvDateofBirth.getText().toString().trim().isEmpty()) {
            showToast("Please select your date of birth");
            return false;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }



    private void showBackConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Heads up!");
        builder.setMessage("If you go back now, you'll be logged out\n" +
                "and everything you've filled will be lost.\n" +
                "Want to continue?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
 // Close the activity
                onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // Dismiss the dialog
            }
        });
        // Show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void logRegistrationEvent(String eventName, String status) {
        Bundle bundle = new Bundle();
        bundle.putString("status", status);  // Success, Failed, or Started
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    private Bitmap blurAadhaarNumber(Uri imageUri, Text visionText) {
        try {
            Log.d("ImageDebug", "Starting blur process for URI: " + imageUri.toString());

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            if (bitmap == null) {
                Log.e("ImageDebug", "Original bitmap is null");
                return null;
            }

            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            if (mutableBitmap == null) {
                Log.e("ImageDebug", "Mutable bitmap is null");
                return null;
            }

            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            boolean aadhaarFound = false;
            int blocksProcessed = 0;

            for (Text.TextBlock block : visionText.getTextBlocks()) {
                blocksProcessed++;
                for (Text.Line line : block.getLines()) {
                    String lineText = line.getText();
                    Log.d("ImageDebug", "Processing line: " + lineText);

                    if (AADHAAR_PATTERN.matcher(lineText).matches()) {
                        Log.d("ImageDebug", "Aadhaar pattern matched: " + lineText);
                        aadhaarFound = true;

                        // Aadhaar number matched, now blur only first 2 segments (8 digits)
                        int count = 0;
                        for (Text.Element element : line.getElements()) {
                            if (count < 2) { // Blur first two elements i.e., 1234 and 5678
                                Rect boundingBox = element.getBoundingBox();
                                if (boundingBox != null) {
                                    Log.d("ImageDebug", "Blurring element " + count + ": " + element.getText());
                                    canvas.drawRect(boundingBox, paint);
                                }
                            }
                            count++;
                        }
                    }
                }
            }

            Log.d("ImageDebug", "Blur process completed. Blocks processed: " + blocksProcessed + ", Aadhaar found: " + aadhaarFound);
            return mutableBitmap;

        } catch (IOException e) {
            Log.e("ImageDebug", "IOException in blurAadhaarNumber", e);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e("ImageDebug", "General exception in blurAadhaarNumber", e);
            e.printStackTrace();
            return null;
        }
    }

    // Add this method if you don't have it
    private File bitmapToFile(Bitmap bitmap, String filename) {
        try {
            File file = new File(getCacheDir(), filename);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            Log.e("ImageDebug", "Error creating file from bitmap", e);
            e.printStackTrace();
            return null;
        }
    }

    // 🔠 Capitalize each word
    private String capitalizeEachWord(String phrase) {
        String[] words = phrase.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return result.toString().trim();
    }
    private void resetSelection() {
        for (LinearLayout layout : allSelectableLayouts) {
            layout.setBackgroundResource(R.drawable.round_corner_dialog);
            layout.setSelected(false); // yeh add karna zaruri hai
        }
    }


    // Inside your activity, e.g., MainActivity.java
    public void welcomeDialogLast(){
        final Dialog dialog = new Dialog(LastPageUserDocumentRegisteration.this);
        dialog.setContentView(R.layout.thanks_neighbrsnook_layout_step_two);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(LastPageUserDocumentRegisteration.this,
                android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        FrameLayout frm_choose=dialog.findViewById(R.id.post_frm);
        //  tvMessage.setText(message);
        dialog.show();
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // PrefMananger.saveScreen(context,"");
                startActivity(new Intent(LastPageUserDocumentRegisteration.this, MainActivity.class));

                finishAffinity();

            }
        });
    }
    private void toggleSection() {
        if (isExpanded) {
            // Section ko hide karo
            doc_section.setVisibility(GONE);
            isExpanded = false;
        } else {
            // Section ko show karo
            doc_section.setVisibility(VISIBLE);
            isExpanded = true;
        }
    }
    private boolean validateGenderAndDob() {
        // gender validation
        if (select_gender.getText().toString().trim().isEmpty()) {
            showToast("Please select gender");
            return false;
        }

        // dob validation
        if (tvDateofBirth.getText().toString().trim().isEmpty()) {
            showToast("Please select your date of birth");
            return false;
        }

        return true;
    }

}
