package com.app_neighbrsnook.nearBy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.NearAdapter;
import com.app_neighbrsnook.adapter.SliderAdapterExample;
import com.app_neighbrsnook.model.OfferModel;
import com.app_neighbrsnook.model.PopularModel;
import com.app_neighbrsnook.model.SellModel;
import com.app_neighbrsnook.model.SellParenetModel;
import com.app_neighbrsnook.libraries.slider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NearByActivity extends AppCompatActivity implements LocationListener {
    RecyclerView rv_nearby;
    NearAdapter nearAdapter;
    ImageView back_btn,searchImageView, addImageView;
    TextView titleTv, locationTv, addressTv;
    List<PopularModel> popularList = new ArrayList<>();
    List<OfferModel> offersList = new ArrayList<>();
    List<OfferModel> checkoutList = new ArrayList<>();
    List<SellParenetModel> saleListModelList = new ArrayList<>();
    List<SellModel> todayListModelList = new ArrayList<>();
    private int[] images;
    private String[] text;
    private SliderAdapterExample adapter;
    private SliderView sliderView;
    RelativeLayout location_rl;
    LinearLayout location_ll;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);
        rv_nearby = findViewById(R.id.rv_nearby);
        location_ll = findViewById(R.id.location_ll);
        locationTv = findViewById(R.id.location);
        location_rl = findViewById(R.id.location_rl);
        addressTv = findViewById(R.id.address);

        titleTv = findViewById(R.id.title);
        titleTv.setText("Near By");
        images= new int[]{R.drawable.saloon_img, R.drawable.restaurent_img, R.drawable.spa_img};
        text=new String[]{"Salon","Restaurant","Spa"};

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        searchImageView = findViewById(R.id.search_imageview);
        addImageView = findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        rv_nearby.setLayoutManager(linearLayoutManager);
        nearAdapter = new NearAdapter(getSaleData(),getTodayData(),getpopularData(), getOffers(), getCheckoutData(), images, text);
        rv_nearby.setAdapter(nearAdapter);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        PlacesClient placesClient = Places.createClient(this);
        location_ll.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN")
                        .build(NearByActivity.this);

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });
        location_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                        Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields).setCountry("IN")
                        .build(NearByActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getLastLocation();

            }

    private List<PopularModel> getpopularData()
    {
//        popularList.add(new PopularModel("Salon",R.drawable.saloon, R.color.event));
//        popularList.add(new PopularModel("Restaurant",R.drawable.restaurent, R.color.text_color));
        popularList.add(new PopularModel("Spa",R.drawable.spa, R.color.poll));
        popularList.add(new PopularModel("Gym",R.drawable.gym,R.color.them_color));
//        popularList.add(new PopularModel("Cafe",R.drawable.cafe, R.color.poll));
        popularList.add(new PopularModel("Grocery",R.drawable.grocery,R.color.them_color));
        popularList.add(new PopularModel("Real Estate",R.drawable.real_estate, R.color.event));
        popularList.add(new PopularModel("Pet Care",R.drawable.pet_care_icon, R.color.text_color));
        return popularList;
    }

    private List<SellParenetModel> getSaleData()
    {
//        saleListModelList.add(new SellParenetModel("Today's Listing", SellParenetModel.TODAY_LIST));
        saleListModelList.add(new SellParenetModel("", SellParenetModel.SLIDER));
        saleListModelList.add(new SellParenetModel("", SellParenetModel.POPULAR_CAT));
        saleListModelList.add(new SellParenetModel("Offers of the week", SellParenetModel.WISH_LIST));
        saleListModelList.add(new SellParenetModel("You may want to checkout", SellParenetModel.YOUR_ITEMS));
        return saleListModelList;
    }

    private List<SellModel> getTodayData()
    {
        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Sudhanshu", "Ceramic antique flower Pot", "Noida 104","2.4 km",R.drawable.flower_pot, SellModel.TODAY_LIST, "1000", "JS 12"));
        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Arsad", "Table", "Noida 102","5 km",R.drawable.table_image, SellModel.TODAY_LIST, "2000(Fixed price)", "aaa"));
        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Amit", "Chair", "Noida 16","2 km",R.drawable.chair_image, SellModel.TODAY_LIST, "3000", ""));
        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Raj", "Watch", "Noida 1","10 km",R.drawable.watch_image, SellModel.TODAY_LIST,"1500", ""));
//        todayListModelList.add(new SellModel(SellModel.TODAY_LIST,"Sudhanshu", "Painting", "Noida 2","5 km",R.drawable.painting_image, SellModel.TODAY_LIST,"9000",""));
        return todayListModelList;
    }

    private List<OfferModel> getOffers()
    {
        offersList.add(new OfferModel("Starbucks","Buffet offers from ₹1199",R.drawable.restaurent_img));
        offersList.add(new OfferModel("Jawed Habib Hair & Beauty", "Offers on facials \nStarts @ ₹599 for 1 person", R.drawable.jh_img));
        offersList.add(new OfferModel("Domino's Pizza","Starting From : ₹100", R.drawable.restaurent_img));
        offersList.add(new OfferModel("Master Of Malts","Starting From : ₹699", R.drawable.cafe_bakery_icon));

        return offersList;
    }

    private List<OfferModel> getCheckoutData()
    {
        checkoutList.add(new OfferModel("Starbucks","Buffet offers from ₹1199.",R.drawable.restaurent_img));
        checkoutList.add(new OfferModel("Jawed Habib Hair & Beauty", "Offers on facials \nStarts @ ₹599 for 1 person", R.drawable.jh_img));
        checkoutList.add(new OfferModel("Domino's Pizza","Starting From : ₹100", R.drawable.restaurent_img));
        checkoutList.add(new OfferModel("Master Of Malts","Starting From : ₹699", R.drawable.cafe_bakery_icon));

        return checkoutList;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
    }
    @Override
    public void onFlushComplete(int requestCode) {
    }
    @Override
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


    private void getLastLocation() {
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
    }

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