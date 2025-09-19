package com.app_neighbrsnook.profile;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.RegistrationGenderAdapter;
import com.app_neighbrsnook.adapter.SelectionNeighbrhoodAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.DocsDataPojo;
import com.app_neighbrsnook.pojo.GroupResponseListPojo;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.StateDropdownPojo;
import com.app_neighbrsnook.utils.FileUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
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

public class ProfileUpdateDocumentUser extends AppCompatActivity implements View.OnClickListener, RegistrationGenderAdapter.CategoryInterface1, ImageUploadAdapter.ImageRequest, LocationListener {

    FrameLayout frmSubmitBtn, uload_reach_out, frmPassport, frmVoterId, frmDl;
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
    SelectionNeighbrhoodAdapter selectionNeighbrhoodAdapter;
    TextView yourNeighbrhood;
    int PERMISSION_ID = 44;
    TextView locationTv, addressTv, useCurrentLocation;
    LinearLayout currentLocationIcon;
    Activity activity;
    ProgressBar progressBar;
    EditText edt_address1, edt_address2, edt_pincode;
    TextView country_tv, city_tv, state_tv, currentLocationIconTv;
    String countryName, stateName, cityName, dobString, genderStr, locationNeighbrhood, address1, address2, stUploadDocument, stAddress, stAddress2;
    String cityGet, stateGet, pincodeGet;
    List<Nbdatum> statelist = new ArrayList<>();
    List<Nbdatum> citylist = new ArrayList<>();
    Dialog country_dialog;
    int countryid, cityId, stateid, nbdId;
    HashMap<String, Object> hm = new HashMap<>();
    SharedPrefsManager sm;
    ImageView adhar_front_img, adhar_back_img, passport_front, passport_back, voter_front, voter_back, imgDlFront, imgDltBack;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    ImageUploadAdapter uploadAdapter;
    private Context mContext = ProfileUpdateDocumentUser.this;
    EditText edt_area1;
    FrameLayout frmStepId, genderFrm, dobFrm;
    ArrayList<StateDropdownPojo> selectNeighbrhood = new ArrayList<>();
    TextView tv, changeLocationTv, select_gender, tvDateofBirth;
    String value, profileNeighbrhood;
    LinearLayout changeActivityRedirect, lnrNeighbrhoodUi, location_childChangelocation;
    ImageView imgAdharFront, imgAdharBack, imgPassportFront, imgPassportBack;
    String FIRST_IMAGE = "FirstImage";
    String SECOND_IMAGE = "SecondImage";
    String CLICK_ON = "";
    private static final int PIC_CROP_REQUEST = 2;
    TextView tv_take_photo, tv_choose_photo, tv_cancel;
    String nghString, fromProfile, source;
    TextView nghId, tvNext;
    CardView cardGender;
    HashMap<String, Object> hashMap;
    TextView tvAboutYou;
    String area1;

    Bitmap bitmap;
    Uri filePath;
    Bitmap bitmap1;
    Bitmap bitmap2;
    String area;

    Uri filePath1;
    Uri filePath2;
    String selectedIdType = "";

    String currentPath = "";
    Uri imageUri;
    CardView lnrBack, lnrFront;
    RadioButton rdAdharCard, rdPassport, rdVoter, rdDl, rentLease;
    private static final Pattern AADHAAR_PATTERN = Pattern.compile("\\b\\d{4}\\s\\d{4}\\s\\d{4}\\b");
    LocationRequest locationRequest;
    private boolean isManualLocationSelected = false;
    TextView addressProofNghbrhod,frontid;
    private boolean shouldShowWelcomeDialog = true;
    LinearLayout frmAdharCard;
    LinearLayout rentLeaseLnr, driverLicenseLnr, voterIdLnr, passportLnr, adharcardLnr;
    private LinearLayout[] allSelectableLayouts;
    String stState, stCity, stPincode, dob, address, addressOne, addressTwo, genderProfile, whatDoYou, location_neighbrhood;
    boolean isFrontUploaded = false;
    boolean isBackUploaded = false;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        sm = new SharedPrefsManager(this);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        PlacesClient placesClient = Places.createClient(this);
        calendar = Calendar.getInstance();
        Bundle extras = getIntent().getExtras();
//        profileNeighbrhood=extras.getString("neighbrhood");
        Intent i = getIntent();
        if (extras != null) {
            if (i != null) {
                source = i.getStringExtra("source");
                stUploadDocument = i.getStringExtra("stUploadDocument");
                profileNeighbrhood = i.getStringExtra("neighbrhood");
            }
        }
        setContentView(R.layout.activity_update_document_user);
        rentLeaseLnr = findViewById(R.id.rentLeaseLnr);
        driverLicenseLnr = findViewById(R.id.driverLicencseLnr);
        frontid = findViewById(R.id.frontid);
        voterIdLnr = findViewById(R.id.voterIdLnr);
        passportLnr = findViewById(R.id.passportLnr);
        adharcardLnr = findViewById(R.id.adharcardLnr);
        addressProofNghbrhod = findViewById(R.id.addressProofNghbrhod);

        PrefMananger.saveScreen(context, PrefMananger.ADDRESS_PROOF);
        lnrFront = findViewById(R.id.lnrFront);
        lnrBack = findViewById(R.id.lnrBack);
        rentLease = findViewById(R.id.rentLease);
        recy_selecy_neighbrhood = findViewById(R.id.select_neighbrhood_recy);
        changeActivityRedirect = findViewById(R.id.location_child);
        frmVoterId = findViewById(R.id.frmVoterId);
        genderFrm = findViewById(R.id.genderFrm);
        imgVoterFront = findViewById(R.id.imgVoterFront);
        imgVotertBack = findViewById(R.id.imgVotertBack);
        location_childChangelocation = findViewById(R.id.location_childChangelocation);
        tvNext = findViewById(R.id.tvNext);
        frmSubmitBtn = findViewById(R.id.upload_id);
        imgAdharBack = findViewById(R.id.imgAdharBack);
        imgAdharFront = findViewById(R.id.imgAdharFront);
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
        voter_back = findViewById(R.id.voter_back);
        uload_reach_out = findViewById(R.id.uload_reach_out);
        edt_area1 = findViewById(R.id.edt_area);
        locationTv = findViewById(R.id.locationIcon);
        useCurrentLocation = findViewById(R.id.useCurrentLocation);
        yourNeighbrhood = findViewById(R.id.yourNeighbrhood);
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
        cardGender = findViewById(R.id.cardGender);
        tvAboutYou = findViewById(R.id.tvAboutYou);
        imgPassportFront = findViewById(R.id.imgPassportFront);
        imgPassportBack = findViewById(R.id.imgPassportBack);
        frmStepId = findViewById(R.id.frmStepId);
        select_gender = findViewById(R.id.select_gender_frm);


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // <- yeh yahan add karein
        state_tv.setOnClickListener(this);
        city_tv.setOnClickListener(this);
        frmStepId.setVisibility(View.GONE);
        tvNext.setText("Update");
        dobString = i.getStringExtra("dobb");
        genderStr = i.getStringExtra("gender");
        fromProfile = i.getStringExtra("source");
        frmAdharCard.setVisibility(View.VISIBLE);
        if (("profile".equalsIgnoreCase(source))) {
            frmSubmitBtn.setVisibility(View.VISIBLE);
            stUploadDocument = i.getStringExtra("stUploadDocument");
            Log.d("ssdfsdfsdsdse",source);

        } else if ("wall".equalsIgnoreCase(source)){
            stUploadDocument = i.getStringExtra("stUploadDocument");
            Log.d("sdsdse",source);
        }
        if (stUploadDocument != null) { // Null check to avoid crash
            resetSelection(); // Pehle sabka background reset kar do
            switch (stUploadDocument) {
                case "Aadhaar Card":
                    adharcardLnr.setBackgroundResource(R.drawable.card_select_document);
                    adharcardLnr.setSelected(true);
                    break;
                case "Passport":
                    passportLnr.setBackgroundResource(R.drawable.card_select_document);
                    passportLnr.setSelected(true);
                    break;
                case "Voter":
                    voterIdLnr.setBackgroundResource(R.drawable.card_select_document);
                    voterIdLnr.setSelected(true);
                    break;
                case "Driving License":
                    driverLicenseLnr.setBackgroundResource(R.drawable.card_select_document);
                    driverLicenseLnr.setSelected(true);
                    break;
                case "Rent Document":
                    rentLeaseLnr.setBackgroundResource(R.drawable.card_select_document);
                    rentLeaseLnr.setSelected(true);
                    lnrBack.setVisibility(View.GONE); // Pehle ki tarah rent wale case me back hide
                    break;
            }
        } else {
            Log.d("UploadDocument", "stUploadDocument is null");
        }
        profileGetData();
        select_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGender();
                edt_address1.clearFocus(); // Clears focus from the EditText
                edt_address1.setCursorVisible(false);

            }
        });
        img_back_address_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                //showBackConfirmationDialog();
            }
        });


        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
                photo_dialog_open_rl.setVisibility(View.GONE);
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                photo_dialog_open_rl.setVisibility(View.GONE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo_dialog_open_rl.setVisibility(View.GONE);
            }
        });
        allSelectableLayouts = new LinearLayout[]{
                rentLeaseLnr, passportLnr, voterIdLnr, driverLicenseLnr, adharcardLnr
        };
        shouldShowWelcomeDialog = false;
        littleMoreSkip1();

        imgAdharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageDebug", "Front image clicked!");
                if (GlobalMethods.checkCameraAndGalleryPermission(ProfileUpdateDocumentUser.this)) {
                    if (imgAdharFront.getDrawable() == null) {
                        Log.d("ImageDebug", "Front image drawable is null, opening camera/gallery");
                        CLICK_ON = FIRST_IMAGE;
                        photo_dialog_open_rl.setVisibility(View.VISIBLE);
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
                                Toast.makeText(ProfileUpdateDocumentUser.this, "Image not available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("ImageDebug", "Drawable is not BitmapDrawable");
                            Toast.makeText(ProfileUpdateDocumentUser.this, "Image not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        imgAdharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageDebug", "Back image clicked!");
                if (GlobalMethods.checkCameraAndGalleryPermission(ProfileUpdateDocumentUser.this)) {
                    if (imgAdharBack.getDrawable() == null) {
                        Log.d("ImageDebug", "Back image drawable is null, opening camera/gallery");
                        CLICK_ON = SECOND_IMAGE;
                        photo_dialog_open_rl.setVisibility(View.VISIBLE);
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
                                Toast.makeText(ProfileUpdateDocumentUser.this, "Image not available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("ImageDebug", "Drawable is not BitmapDrawable");
                            Toast.makeText(ProfileUpdateDocumentUser.this, "Image not available", Toast.LENGTH_SHORT).show();
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
                frmAdharCard.setVisibility(View.VISIBLE);
                lnrFront.setVisibility(View.VISIBLE);
                lnrBack.setVisibility(View.GONE);

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

                deselectAll();
                imgAdharFront.setImageDrawable(null);
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(View.VISIBLE);
                lnrFront.setVisibility(View.VISIBLE);
                lnrBack.setVisibility(View.VISIBLE);
                frontid.setText("Front");

            }
        });
        voterIdLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                voterIdLnr.setBackgroundResource(R.drawable.card_select_document);
                voterIdLnr.setSelected(true);

                deselectAll();
                imgAdharFront.setImageDrawable(null);
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(View.VISIBLE);
                lnrFront.setVisibility(View.VISIBLE);
                lnrBack.setVisibility(View.VISIBLE);
                frontid.setText("Front");

            }
        });
        driverLicenseLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                driverLicenseLnr.setBackgroundResource(R.drawable.card_select_document);
                driverLicenseLnr.setSelected(true);

                deselectAll();
                imgAdharFront.setImageDrawable(null);
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(View.VISIBLE);
                lnrFront.setVisibility(View.VISIBLE);
                lnrBack.setVisibility(View.VISIBLE);
                frontid.setText("Front");

            }
        });
        adharcardLnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
                adharcardLnr.setBackgroundResource(R.drawable.card_select_document);
                adharcardLnr.setSelected(true);

                deselectAll();
                imgAdharFront.setImageDrawable(null);
                imgAdharBack.setImageDrawable(null);
                frmAdharCard.setVisibility(View.VISIBLE);
                lnrFront.setVisibility(View.VISIBLE);
                lnrBack.setVisibility(View.VISIBLE);
                frontid.setText("Front");

            }
        });


        DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                date1();
            }
        };
        tvDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(ProfileUpdateDocumentUser.this, fromdate, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                pickerDialog.show();
                edt_address2.clearFocus(); // Clears focus from the EditText
                edt_address2.setCursorVisible(false);
                edt_address1.clearFocus();
                edt_address1.setCursorVisible(false);
            }
        });
        dobFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(ProfileUpdateDocumentUser.this, fromdate, calendar.get(Calendar.YEAR),
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
        country_tv.setText("India");

        viewDocumentsUser(hm);

        frmSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   if (CheckAllFields()) {
                addressProofDocumentSubmit();
                //   awaitStatus();
                  }
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("TAG_status", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                }
                return;
            }
            if (requestCode == 1) {
                Log.e("1258963", bitmap + "");
                File file1 = bitmapToFile(bitmap, getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg");
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
                    imgAdharFront.setVisibility(View.VISIBLE);
                    imgAdharFront.setImageBitmap(bitmap);
                    imgAdharFront.setVisibility(View.VISIBLE);
                    ImagePOJO imagePOJO = new ImagePOJO();
                    imagePOJO.bitmap = bitmap;
                    imagePOJO.imageUri = filePath;
                } else if (CLICK_ON.equals(SECOND_IMAGE)) {
                    bitmap2 = bitmap;
                    filePath2 = filePath;
                    imgAdharBack.setImageBitmap(bitmap);
                    imgAdharBack.setVisibility(View.VISIBLE);
                    imgAdharBack.setVisibility(View.VISIBLE);
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
                        } else if (CLICK_ON.equals(SECOND_IMAGE)) {
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
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
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


    // Resume par bhi check kar sakte hai (optional)
    @Override
    public void onResume() {
        super.onResume();
        // Agar chahte hai ki resume par bhi check ho
        // if (!checkPermissions() || !isLocationEnabled()) {
        //     showLocationPermissionDialog();
        // }
    }


    private void viewDocumentsUser(HashMap<String, Object> hm) {
        Boolean isInternetConnection = GlobalMethods.checkConnection(context);
        if (isInternetConnection) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            hm.put("userid", sm.getString("user_id"));
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<GroupResponseListPojo> call = service.uploadDocuments("uploaddocs", hm);
            call.enqueue(new Callback<GroupResponseListPojo>() {
                @Override
                public void onResponse(Call<GroupResponseListPojo> call, Response<GroupResponseListPojo> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        String status = response.body().getStatus();
                        if ("success".equalsIgnoreCase(status)) {
                            DocsDataPojo docsDataPojo = response.body().getDocsdata();
                            if (docsDataPojo != null) {
                                // Reset Images before setting
                                resetImageViews();

                                if (docsDataPojo.getAadhar_front().isEmpty()) {
                                    imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap1 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getAadhar_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharFront);
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getAadhar_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap1 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }
                                if (docsDataPojo.getAadhar_back().isEmpty()) {
                                    imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap2 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getAadhar_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharBack);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getAadhar_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap2 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }

                                if (docsDataPojo.getPassport_front().isEmpty()) {
                                    imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap1 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getPassport_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharFront);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getPassport_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap1 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }

                                if (docsDataPojo.getPassport_back().isEmpty()) {
                                    imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap2 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getPassport_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharBack);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getPassport_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap2 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }

                                if (docsDataPojo.getVoterid_front().isEmpty()) {
                                    imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap1 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getVoterid_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharFront);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getVoterid_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap1 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }

                                if (docsDataPojo.getVoterid_back().isEmpty()) {
                                    imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap2 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getVoterid_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharBack);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getVoterid_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap2 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }


                                if (docsDataPojo.getDriving_license_front().isEmpty()) {
                                    imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap1 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getDriving_license_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharFront);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getDriving_license_front())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap1 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }
                                if (docsDataPojo.getDriving_license_back().isEmpty()) {
                                    imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap2 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getDriving_license_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharBack);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getDriving_license_back())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap2 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }
// Rent Docs id prooof
                                if (docsDataPojo.getRent_docs().isEmpty()) {
                                    imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
                                    bitmap1 = bitmap; // Agar `bitmap` already initialized hai.
                                } else {
                                    Glide.with(context)
                                            .load(docsDataPojo.getRent_docs())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(imgAdharFront);

                                    // Aadhar Front image ko Bitmap me convert karke `bitmap1` set karna
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(docsDataPojo.getRent_docs())
                                            .apply(RequestOptions.centerCropTransform())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    bitmap1 = resource; // Bitmap set karna
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Placeholder ke liye kuch nahi karna
                                                }
                                            });
                                }

// Baaki images load karne ke liye function call
                                loadImageWithNoCache(docsDataPojo.getAadhar_front(), imgAdharFront, "Aadhar Front");
                                loadImageWithNoCache(docsDataPojo.getAadhar_back(), imgAdharBack, "Aadhar Back");
                                loadImageWithNoCache(docsDataPojo.getPassport_front(), imgAdharFront, "Passport Front");
                                loadImageWithNoCache(docsDataPojo.getPassport_back(), imgAdharBack, "Passport Back");
                                loadImageWithNoCache(docsDataPojo.getVoterid_front(), imgAdharFront, "Voter Front");
                                loadImageWithNoCache(docsDataPojo.getVoterid_back(), imgAdharBack, "Voter Back");
                                loadImageWithNoCache(docsDataPojo.getDriving_license_front(), imgAdharFront, "DL Front");
                                loadImageWithNoCache(docsDataPojo.getDriving_license_back(), imgAdharBack, "DL Back");
                                loadImageWithNoCache(docsDataPojo.getRent_docs(), imgAdharFront, "Rent Docs");


                            } else {
                                Toast.makeText(ProfileUpdateDocumentUser.this, "No document data available.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProfileUpdateDocumentUser.this, "Failed to load documents: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfileUpdateDocumentUser.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GroupResponseListPojo> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("API_ERROR", t.toString());
                    Toast.makeText(ProfileUpdateDocumentUser.this, "API Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            GlobalMethods.getInstance(this).globalDialog(context, "No internet connection.");
        }
    }

    // Method to Reset ImageViews to Default
    private void resetImageViews() {
        imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
        imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
        imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
        imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
        imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
        imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
        imgAdharFront.setImageResource(R.drawable.marketplace_white_background);
        imgAdharBack.setImageResource(R.drawable.marketplace_white_background);
    }

    // Method to Load Image with No Cache
    private void loadImageWithNoCache(String url, ImageView imageView, String logTag) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.marketplace_white_background);
            Log.d("ImageStatus", logTag + " is empty or null");
        } else {
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) // Disable caching
                    .networkPolicy(NetworkPolicy.NO_CACHE) // Force network loading
                    .error(R.drawable.marketplace_white_background)
                    .placeholder(R.drawable.marketplace_white_background)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("ImageStatus", logTag + " image loaded successfully");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("ImageStatus", logTag + " image load failed", e);
                        }
                    });
        }
    }

    private void imageDialog(Bitmap bitmap) {
        ImageView cancel, iv_uploaded_image, iv_remove;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.address_proof_dialog_layout);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        iv_remove = image_dialog.findViewById(R.id.iv_remove_image);
        iv_uploaded_image = image_dialog.findViewById(R.id.iv_uploaded_image);
        iv_uploaded_image.setImageBitmap(bitmap);
        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bitmap1=null;
                if (type == 1) {
                    bitmap1 = null;
                    iv_uploaded_image.setImageDrawable(null);
                    imgAdharFront.setImageDrawable(null);
                    imgAdharFront.setVisibility(View.VISIBLE);
                /*    l_adhar_front.setVisibility(View.VISIBLE);
                    adhar_front_img.setVisibility(View.GONE);*/
                } else if (type == 2) {
                    bitmap2 = null;
                    iv_uploaded_image.setImageDrawable(null);
                    imgAdharBack.setImageDrawable(null);
                    imgAdharBack.setVisibility(View.VISIBLE);
                    //  adhar_back_img.setVisibility(View.GONE);
                }
                image_dialog.dismiss();/*
                l_adhar_front.setVisibility(View.VISIBLE);
                adhar_front_img.setVisibility(View.GONE);
                frm_adharBack.setVisibility(View.VISIBLE);
                adhar_back_img.setVisibility(View.GONE);*/

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });
        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ProfileUpdateDocumentUser.this, android.R.color.white)));
        image_dialog.setCancelable(true);
        image_dialog.show();
    }

    private void selectGender() {
        RecyclerView rv;
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.selct_genderr);
        rv = mail_dialog.findViewById(R.id.rv_category);
//        confirm = mail_dialog.findViewById(R.id.cross_imageview);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RegistrationGenderAdapter emailListAdapter = new RegistrationGenderAdapter(mail_dialog, ProfileUpdateDocumentUser.this);
        rv.setAdapter(emailListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();

            }
        });
        mail_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
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

    public void welcomeDialog() {
        final Dialog dialog = new Dialog(ProfileUpdateDocumentUser.this);
        dialog.setContentView(R.layout.thanks_neighbrsnook_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ProfileUpdateDocumentUser.this,
                android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView frmCancel = dialog.findViewById(R.id.tvCancel);
        TextView frm_choose = dialog.findViewById(R.id.post_frm);
        //littleMoreSkip1();
        TextView nghName = dialog.findViewById(R.id.nghName);
        TextView tvcancel = dialog.findViewById(R.id.tvCancel);
        nghName.setText(nghString);
        // tvMessage.setText(message);
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
                welcomeDialogLast();
            }
        });
    }

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

    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getString(R.string.app_name);
            File directory = new File(path);
            // File sd_directory   = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            //  sd_directory   =  getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave);
            file.createNewFile();
            String destination = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//Convert bitmap to byte array
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

    private void performCrop(Uri fileUri) {
        try {
            CropImage.ActivityBuilder a1 = CropImage.activity(fileUri);
            a1.setCropMenuCropButtonTitle("Set Image");
            a1.setAspectRatio(1, 1);
            a1.setFixAspectRatio(false);

            a1.start(activity);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
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
            // sm.setString("neighbrhood", String.valueOf(edt_area1));
            //  hashMap.put("type", RequestBody.create(MultipartBody.FORM, "changeneighborhood"));
            if (select_gender.getText().toString().equals("Male")) {
                hashMap.put("gender", RequestBody.create(MultipartBody.FORM, "1"));
            } else if (select_gender.getText().toString().equals("Female")) {
                hashMap.put("gender", RequestBody.create(MultipartBody.FORM, "2"));
            } else if (select_gender.getText().toString().equals("Other")) {
                hashMap.put("gender", RequestBody.create(MultipartBody.FORM, "3"));
            }
            ApiExecutor.getApiService().addressProofPhoto("reg-step-III", aadharFront, aadharBack, hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        //  Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getStatus().equals("success")) {
                            dialog.dismiss();
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
                    // Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
//        dialog.setCanceledOnTouchOutside(true);
    }


    private MultipartBody.Part createMultipartBodyPart(Uri filePath, Bitmap bitmap, String key) {
        File file;
        if (filePath != null) {
            try {
                file = FileUtil.from(this, filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (bitmap != null) {
            file = bitmapToFile(bitmap, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
        } else {
            return null;
        }

        // Compress the file if it's an image
        compressImage(file);

        RequestBody requestBody = RequestBody.create(MediaType.parse(".jpg"), file);
        return MultipartBody.Part.createFormData(key, file.getName(), requestBody);
    }

    private void compressImage(File file) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            try (FileOutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out); // Adjust compression ratio as needed
            }
        } catch (Exception e) {
            Log.e("ERROR", "Error compressing file: " + e);
        }
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
                Toast.makeText(ProfileUpdateDocumentUser.this, "Data not found", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
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
        Toast.makeText(ProfileUpdateDocumentUser.this, message, Toast.LENGTH_SHORT).show();
    }


    /*@Override
    public void onBackPressed() {
        showBackConfirmationDialog(); // Call the common method to show the dialog
    }*/

    private void showBackConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to go back? Unsaved changes might be lost.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish(); // Close the activity
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // Dismiss the dialog
            }
        });

        // Show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void welcomeDialogLast() {
        final Dialog dialog = new Dialog(ProfileUpdateDocumentUser.this);
        dialog.setContentView(R.layout.thanks_neighbrsnook_layout_step_two);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ProfileUpdateDocumentUser.this,
                android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        FrameLayout frm_choose = dialog.findViewById(R.id.post_frm);
        //  tvMessage.setText(message);
        dialog.show();
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // PrefMananger.saveScreen(context,"");
                startActivity(new Intent(ProfileUpdateDocumentUser.this, MainActivity.class));

                finishAffinity();

            }
        });
    }

    private void deselectAll() {
        if (rdAdharCard != null) rdAdharCard.setChecked(false);
        if (rdPassport != null) rdPassport.setChecked(false);
        if (rdVoter != null) rdVoter.setChecked(false);
        if (rdDl != null) rdDl.setChecked(false);
        if (rentLease != null) rentLease.setChecked(false);

        // Bitmap ko null set aur recycle karna (Memory free)
        if (bitmap1 != null) {
            bitmap1.recycle();
            bitmap1 = null;
        }
        if (bitmap2 != null) {
            bitmap2.recycle();
            bitmap2 = null;
        }
        // ImageView ko null set karna (Bitmap clear karna)
        if (imgAdharFront != null) imgAdharFront.setImageDrawable(null);
        if (imgAdharBack != null) imgAdharBack.setImageDrawable(null);

    }

    // Show Settings Dialog for "Don't Ask Again"
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("The app needs your location permission to assign you a neighbourhood.");
        builder.setPositiveButton("Go to Settings", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
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
    private void resetSelection() {
        adharcardLnr.setBackgroundResource(R.drawable.card_unselect_document);
        passportLnr.setBackgroundResource(R.drawable.card_unselect_document);
        voterIdLnr.setBackgroundResource(R.drawable.card_unselect_document);
        driverLicenseLnr.setBackgroundResource(R.drawable.card_unselect_document);
        rentLeaseLnr.setBackgroundResource(R.drawable.card_unselect_document);

        adharcardLnr.setSelected(false);
        passportLnr.setSelected(false);
        voterIdLnr.setSelected(false);
        driverLicenseLnr.setSelected(false);
        rentLeaseLnr.setSelected(false);
    }

    private void profileGetData() {
        hashMap = new HashMap<>();
        hashMap.put("loggeduser", sm.getString("user_id"));
        userProfileGetData(hashMap);
    }

    private void userProfileGetData(HashMap<String, Object> hm) {
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            //step2 rgn Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(PrefMananger.GetLoginData(context).getId()), hm);
            Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(sm.getString("user_id")), hm);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    dialog.dismiss();
                    JsonElement jsonElement = response.body();
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    try {
                        //sm.setString("userphoto", jsonObject.get("userpic").getAsString());
                        location_neighbrhood = jsonObject.get("neighborhood").getAsString();
                        address = jsonObject.get("address").getAsString();
                        addressOne = jsonObject.get("addressone").getAsString();
                        stState = jsonObject.get("state").getAsString();
                        stCity = jsonObject.get("city").getAsString();
                        stPincode = jsonObject.get("pincode").getAsString();
                        dob = jsonObject.get("dob").getAsString();
                        genderProfile = jsonObject.get("gender").getAsString();
                        //   whatDoYou = jsonObject.get("nbrs_type").getAsString();
                        stUploadDocument = jsonObject.get("uploaded_doc").getAsString();

                        tvDateofBirth.setText(dob);
                        select_gender.setText(genderProfile);
                      /*  Log.d("sdfsaee",stState);
                        Log.d("sdfsaee",stPincode);
                        Log.d("sdfsaee",stCity );*/


                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    //  GlobalMethods.getInstance(this).globalDialog(getActivity(), "Something seems to have gone wrong.Please try again");
                    dialog.dismiss();
                    Log.d("res", t.getMessage());
                }
            });
        }
    }

    private boolean CheckAllFields() {
        if (adharcardLnr.isSelected()) {
            selectedIdType = "Aadhaar Card";
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
}