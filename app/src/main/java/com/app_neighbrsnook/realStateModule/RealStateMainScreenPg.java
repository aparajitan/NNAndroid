package com.app_neighbrsnook.realStateModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.app_neighbrsnook.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RealStateMainScreenPg extends AppCompatActivity implements LocationListener {
    CardView card_main;
    FrameLayout frm_post_form;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    TextView locationTv, addressTv;
    RelativeLayout location_rl;
    LinearLayout location_ll;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    TextView tv_advertising,tv_view_all;
    CardView cardView;
    TextView tv_real_state,tv_recom;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_state_main_screen_pg);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        cardView=findViewById(R.id.card_view_first);
        location_rl = findViewById(R.id.location_rl);
        location_ll = findViewById(R.id.location_ll);
        locationTv = findViewById(R.id.location);
        addressTv = findViewById(R.id.address);
        tv_advertising=findViewById(R.id.text_view);
        tv_view_all=findViewById(R.id.view_all_post);
       // tv_real_state=findViewById(R.id.real_state_id);
        tv_recom=findViewById(R.id.tv_recomended);
        frm_post_form=findViewById(R.id.post_pg_form_frm);
       // Places.initialize(getApplicationContext(), getString(R.string.api_key));
       // java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        tv_recom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateMainScreenPg.this,PostPropertyRentSellPg.class));
            }
        });
        frm_post_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateMainScreenPg.this,PostPropertyUploadPg.class));
           }
        });
//        PlacesClient placesClient = Places.createClient(this);
      /*  tv_real_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateMainScreenPg.this,WriteReviewRealState.class));
            }
        });*/
        tv_advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateMainScreenPg.this,RealStateFilterPg.class));
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateMainScreenPg.this,RealDetailsScreenMainScreen.class));
            }
        });
        tv_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RealStateMainScreenPg.this,PgViewProperties.class));
            }
        });
        /*location_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN")
                        .build(RealStateMainScreenPg.this);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(NearByActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        */

       /* location_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MapBottomSheetFragment bottomSheetDialog = MapBottomSheetFragment.newInstance();
//                bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN")
                        .build(RealStateMainScreenPg.this);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(NearByActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });*/
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                Log.i("TAG_a", "Place: " + place.getName() + ", " + place.getId() + ","+ place.getAddress()+ ","+ place.getLatLng().latitude +"," +place.getLatLng().longitude +","+place.getLatLng());
                locationTv.setText(place.getName());
                addressTv.setText(place.getAddress());
//                getAddress(place.getLatLng().latitude, place.getLatLng().longitude);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG_status", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    */
   /* private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
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
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }*/
    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            address.setText("Latitude: " + mLastLocation.getLatitude() + "");
            getAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };
    private void getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        List<Address>  addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            String subLocality = addresses.get(0).getSubLocality();

            Log.d("Address",  city +","+ state+","+ postalCode+","+ knownName);

            locationTv.setText(subLocality);
            addressTv.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
//            getLastLocation();
        }
    }
}