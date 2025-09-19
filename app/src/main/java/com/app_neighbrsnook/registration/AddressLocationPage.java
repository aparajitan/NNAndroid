package com.app_neighbrsnook.registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
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
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressLocationPage extends AppCompatActivity implements View.OnClickListener, LocationListener {

    FrameLayout frm_upload, uload_reach_out, frmAdharCard, frmPassport, frmVoterId, frmDl;
    ImageView img_back_address_proof, imgVoterFront, imgVotertBack;
    LinearLayout frm_aadhar_card, lnr_passport, lnr_voter_id;
    //ActivityAddressProofBinding binding;
    Context context;
    RecyclerView recy_selecy_neighbrhood;
    Calendar calendar;
    RelativeLayout photo_dialog_open_rl;
    RadioGroup rgIdProof;
    private static int AUTOCOMPLETE_REQUEST_CODE = 10;
    FusedLocationProviderClient mFusedLocationClient;
    TextView yourNeighbrhood;
    TextView locationTv, addressTv, useCurrentLocation;
    LinearLayout currentLocationIcon;
    Activity activity;
    ProgressBar progressBar;
    EditText edt_address1, edt_address2, edt_pincode;
    TextView country_tv, city_tv, state_tv,et_search_area, currentLocationIconTv;
    SharedPrefsManager sm;
    ImageView adhar_front_img, adhar_back_img, passport_front, passport_back, voter_front, voter_back, imgDlFront, imgDltBack;
    EditText edt_area1;
    String nbdNamearea = "";
    FrameLayout frmStepId, genderFrm, dobFrm;
    private static final int REQUEST = 112;
    TextView tv, changeLocationTv, select_gender, tvDateofBirth;
    LinearLayout location_child, lnrNeighbrhoodUi,lnruseCurrentLocation, location_childChangelocation;
    RadioButton rdAdharCard, rdPassport, rdVoter, rdDl;
    ImageView imgAdharFront, imgAdharBack, imgPassportFront, imgPassportBack;
    private static final int PIC_CROP_REQUEST = 2;
    TextView tv_take_photo, tv_choose_photo, tv_cancel;
    CardView cardSearchArea;
    String st_city, st_state, st_area,st_pincode;
    private boolean isLocationAlreadySet = false;
    private static final int PERMISSION_ID = 44;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        sm = new SharedPrefsManager(this);
        calendar = Calendar.getInstance();
        setContentView(R.layout.activity_address_location_page);
        PrefMananger.saveScreen(context, PrefMananger.ADDRESS_PROOF);
        recy_selecy_neighbrhood = findViewById(R.id.select_neighbrhood_recy);
        lnruseCurrentLocation = findViewById(R.id.lnruseCurrentLocation);
        location_child = findViewById(R.id.location_child);
        cardSearchArea = findViewById(R.id.cardSearchArea);
        et_search_area = findViewById(R.id.et_search_area);
        frmVoterId = findViewById(R.id.frmVoterId);
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
        tv_cancel = findViewById(R.id.cancle_tv);
        tv_take_photo = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
        passport_front = findViewById(R.id.passport_front_img);
        passport_back = findViewById(R.id.passport_back_img);
        voter_front = findViewById(R.id.voter_front);
        frmDl = findViewById(R.id.frmDl);
        rgIdProof = findViewById(R.id.can_join_radgrp_id);
        voter_back = findViewById(R.id.voter_back);
        uload_reach_out = findViewById(R.id.uload_reach_out);
        edt_area1 = findViewById(R.id.edt_area);
        locationTv = findViewById(R.id.location);
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
        imgPassportFront = findViewById(R.id.imgPassportFront);
        imgPassportBack = findViewById(R.id.imgPassportBack);
        frmStepId = findViewById(R.id.frmStepId);
        select_gender = findViewById(R.id.select_gender_frm);
        //stateApi("100");
        // country_tv.setOnClickListener(this);
        state_tv.setOnClickListener(this);
        city_tv.setOnClickListener(this);
        frmStepId.setVisibility(View.GONE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        nbdNamearea = String.valueOf(edt_area1);
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);
       //  GlobalMethods.checkPermission(context);
        getLastLocation();
     /*   currentLocationIconTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,AddressProofLocation.class));
                getLastLocation();
            }
        });*/
        lnruseCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    startActivity(new Intent(context, SecondPageUserLocationRegisteration.class));
                    finish();
                } else {
                    showEnableLocationPermissionDialog();
                }
            }
        });
        et_search_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .setCountry("IN")
                        .setHint("Area Search")
                        .build(AddressLocationPage.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });
        cardSearchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN").setHint("Area Search").build(AddressLocationPage.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        img_back_address_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                onBackPressed();
            }
        });
        location_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN").setHint("Area Search")
                        .build(AddressLocationPage.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }
    double latitude = 0.0;
    double longitude = 0.0;
    String address = "";
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
        if (resultCode == RESULT_OK && requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("place_name", place.getName());
            resultIntent.putExtra("place_address", place.getAddress());
            resultIntent.putExtra("latitude", place.getLatLng().latitude);
            resultIntent.putExtra("longitude", place.getLatLng().longitude);
            resultIntent.putExtra("source", "addresslocation");
            //getAddress(latitude, longitude);

            setResult(RESULT_OK, resultIntent);
            finish();  // Activity ko close karna hai taaki result return ho
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
    }



    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            address.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //getAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

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
                // Log the cleaned address
                Log.d("Cleaned Address", address);
                // Set the cleaned address in the TextView
                addressTv.setText(address);
                // Setting other UI components
                String subLocality = addressObj.getSubLocality();
                locationTv.setText(subLocality);
                edt_pincode.setText(postalCode);
                edt_area1.setText(subLocality);
                state_tv.setText(state);
                city_tv.setText(city);
                st_city = addressObj.getLocality();
                st_state = addressObj.getAdminArea();
                st_area = addressObj.getSubLocality();
                st_pincode = addressObj.getPostalCode();
                if (!isLocationAlreadySet) {
                    currentLocationGet();
                    isLocationAlreadySet = true;
                }
               // getSelectNeighbourHoodList(true);
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
                 //   requestPermissions(); // Ask for permission if not granted
                    return;
                }

                mFusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location = locationResult.getLastLocation();
                                if (location != null) {
                                    Log.d("DEBUG_LOCATION", "Lat: " + location.getLatitude() +
                                            ", Lng: " + location.getLongitude() +
                                            ", Accuracy: " + location.getAccuracy());
                                    getAddress(location.getLatitude(), location.getLongitude());
                                } else {
                                    Log.d("Location", "Failed to get fresh location");
                                    Toast.makeText(getApplicationContext(), "Unable to get current location.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        Looper.getMainLooper()
                );

            } else {
                Toast.makeText(this, "Please enable GPS/location services", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else {
            requestNewLocationData();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestNewLocationData() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // 5 sec
        locationRequest.setFastestInterval(2000);
        locationRequest.setNumUpdates(1); // Only one update needed

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         //   requestPermissions();
            return;
        }

        mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            Log.d("Location", "Fresh location fetched");
                            getAddress(location.getLatitude(), location.getLongitude());
                        } else {
                            Log.d("Location", "Failed to get fresh location");
                        }
                    }
                },
                Looper.getMainLooper()
        );
    }

   /* private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }*/

    @Override
    public void onResume() {
        super.onResume();

        if (checkPermissions()) {
            // Permission already granted, do your location-related work
            getLastLocation(); // Or call whatever method you need
        } else {
            // Don't request permission, directly open settings dialog
         //   showSettingsDialog();
        }
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Check location permissions again to ensure they are granted
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                getAddress(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Location permission denied. Please enable it from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required");
        builder.setMessage("The app needs your location permission to assign you a neighbourhood.");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Optionally close the activity
            }
        });
        builder.show();
    }
    private void currentLocationGet() {
        if (GlobalMethods.checkConnection(this)) {
            final HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", Integer.parseInt(sm.getString("user_id")));
            hashMap.put("country_name", "India");
            hashMap.put("latitude", String.valueOf(latitude));
            hashMap.put("longitude", String.valueOf(longitude));
            hashMap.put("area_name",  st_area);
            hashMap.put("pincode", st_pincode);
            hashMap.put("state_name", st_state);
            hashMap.put("city_name",st_city );

          /*  Log.d("qwrewrewrwrwrwwerwr", String.valueOf(latitude));
            Log.d("qwrewrewrwrwrwwerwr", String.valueOf(longitude));*/
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
                            Toast.makeText(AddressLocationPage.this, resStr, Toast.LENGTH_SHORT).show(); // handle plain string
                        }

                    } catch (Exception e) {
                        Log.e("ParseError", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AddressLocationPage.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            GlobalMethods.getInstance(context).globalDialog(this, "No internet connection.");
        }
    }
    private void showEnableLocationPermissionDialog() {
        new AlertDialog.Builder(AddressLocationPage.this)
                .setTitle("Location Permission Required")
                .setMessage("Please enable location permission from settings to use current location.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
