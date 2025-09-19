package com.app_neighbrsnook;

import static android.Manifest.permission.CALL_PHONE;

import static com.app_neighbrsnook.R.color.them_color;
import static com.app_neighbrsnook.R.color.white;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.adapter.HospitalAdapter;
import com.app_neighbrsnook.model.publicdirectory.Listdatum;
import com.app_neighbrsnook.model.publicdirectory.PublicDirectoryPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicAgencyDirActivity extends AppCompatActivity implements View.OnClickListener, HospitalAdapter.LocationInterface {
    TextView titleTv, neighbrhood_name_tv, hospital_tv, fire_tv, police_tv, ambulance_tv, tvMessage;
    Dialog phone_dialog;
    ImageView back_btn, serch_btn, add_btn;
    LinearLayout fire_ll, hospital_ll, police_ll, ambulance_ll;
    Context context;
    RecyclerView hospital_rv;
    HospitalAdapter hospitalAdapter;
    SharedPrefsManager sm;
    ImageView fire_iv, hospital_iv, police_iv, ambulance_iv;
    //    List<Listdatum> hospitalList = new ArrayList<>();
    List<Listdatum> list = new ArrayList<>();
    List<Listdatum> ambulanceList = new ArrayList<>();
    List<Listdatum> fireBigradeList = new ArrayList<>();
    List<Listdatum> hospitalList = new ArrayList<>();
    List<Listdatum> policeStrationList = new ArrayList<>();
    int tintColoroff, tintColorOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_agency_dir);
        sm = new SharedPrefsManager(this);
        item();
        tintColorOn = ContextCompat.getColor(this, them_color);
        tintColoroff = ContextCompat.getColor(this, white);
        publicDirectoryApi();
        titleTv.setText("Public agency directory");
        hospital_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
        hospital_tv.setTextColor(ContextCompat.getColor(this, white));
        hospital_iv.setImageTintList(ColorStateList.valueOf(tintColoroff));
//        hospital_rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void publicDirectoryApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", sm.getString("user_id"));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<PublicDirectoryPojo> call = service.publicDirApi("publicdirectory", hm);
        call.enqueue(new Callback<PublicDirectoryPojo>() {
            @Override
            public void onResponse(Call<PublicDirectoryPojo> call, Response<PublicDirectoryPojo> response) {
                neighbrhood_name_tv.setText(response.body().getNeighbrhood());
                if (response.body().getMessage().equals("Data Found")) {
                    tvMessage.setVisibility(View.GONE);
                    hospital_rv.setVisibility(View.VISIBLE);
                    list = response.body().getListdata();
                    neighbrhood_name_tv.setText(response.body().getNeighbrhood());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getType().equals("Ambulance")) {
                            ambulanceList.add(list.get(i));
                        }
                        if ((list.get(i).getType().equals("Hospital"))) {
                            hospitalList.add(list.get(i));
                            hospitalAdapter = new HospitalAdapter(hospitalList, PublicAgencyDirActivity.this);
                            hospital_rv.setAdapter(hospitalAdapter);
                        }
                        if (list.get(i).getType().equals("Fire Brigade")) {
                            fireBigradeList.add(list.get(i));
                        }
                        if (list.get(i).getType().equals("Police Station")) {
                            policeStrationList.add(list.get(i));
//                        policeStrationList.add(list.get(i).getName());
                        }
                    }
                } else {

                    tvMessage.setVisibility(View.VISIBLE);
                    hospital_rv.setVisibility(View.GONE);
                    Toast.makeText(PublicAgencyDirActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PublicDirectoryPojo> call, Throwable t) {

                Log.d("res", t.getMessage());
            }
        });
    }

    private void item() {
        hospital_rv = findViewById(R.id.hospital_rv);
        hospital_rv.setLayoutManager(new LinearLayoutManager(this));
        neighbrhood_name_tv = findViewById(R.id.my_neighbourhood);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        fire_ll = findViewById(R.id.fire_ll);
        hospital_ll = findViewById(R.id.hospital_ll);
        police_ll = findViewById(R.id.police_ll);
        ambulance_ll = findViewById(R.id.ambulance_ll);
        serch_btn = findViewById(R.id.search_imageview);
        add_btn = findViewById(R.id.add_imageview);
        serch_btn.setVisibility(View.GONE);
        add_btn.setVisibility(View.GONE);
        back_btn.setOnClickListener(this);
        fire_ll.setOnClickListener(this);
        hospital_ll.setOnClickListener(this);
        police_ll.setOnClickListener(this);
        ambulance_ll.setOnClickListener(this);
        hospital_tv = findViewById(R.id.hospital_tv);
        fire_tv = findViewById(R.id.fire_tv);
        police_tv = findViewById(R.id.police_tv);
        hospital_iv = findViewById(R.id.hospital_iv);
        police_iv = findViewById(R.id.police_iv);
        fire_iv = findViewById(R.id.fire_iv);
        ambulance_iv = findViewById(R.id.ambulance_iv);
        ambulance_tv = findViewById(R.id.ambulance_tv);
        tvMessage = findViewById(R.id.tvMessage);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.hospital_ll:
                resetColor();
//                hospitalList.clear();
                hospital_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                hospital_tv.setTextColor(ContextCompat.getColor(this, white));
                hospital_iv.setImageTintList(ColorStateList.valueOf(tintColoroff));
//                hospital_rv.setLayoutManager(new LinearLayoutManager(this));
                hospitalAdapter = new HospitalAdapter(hospitalList, PublicAgencyDirActivity.this);
                hospital_rv.setAdapter(hospitalAdapter);
                break;

            case R.id.police_ll:
                resetColor();
                police_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                police_tv.setTextColor(ContextCompat.getColor(this, white));
                police_iv.setImageTintList(ColorStateList.valueOf(tintColoroff));
                hospitalAdapter = new HospitalAdapter(policeStrationList, PublicAgencyDirActivity.this);
                hospital_rv.setAdapter(hospitalAdapter);
                break;

            case R.id.fire_ll:
                resetColor();
                fire_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                fire_tv.setTextColor(ContextCompat.getColor(this, white));
                fire_iv.setImageTintList(ColorStateList.valueOf(tintColoroff));
                hospitalAdapter = new HospitalAdapter(fireBigradeList, PublicAgencyDirActivity.this);
                hospital_rv.setAdapter(hospitalAdapter);
                break;

            case R.id.ambulance_ll:
                resetColor();
                ambulance_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                ambulance_tv.setTextColor(ContextCompat.getColor(this, white));
                ambulance_iv.setImageTintList(ColorStateList.valueOf(tintColoroff));
                hospitalAdapter = new HospitalAdapter(ambulanceList, this);
                hospital_rv.setAdapter(hospitalAdapter);
                break;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void resetColor() {
        hospital_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
        fire_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
        police_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
        hospital_tv.setTextColor(ContextCompat.getColor(this, them_color));
        fire_tv.setTextColor(ContextCompat.getColor(this, them_color));
        police_tv.setTextColor(ContextCompat.getColor(this, them_color));
        hospital_iv.setImageTintList(ColorStateList.valueOf(tintColorOn));
        police_iv.setImageTintList(ColorStateList.valueOf(tintColorOn));
        fire_iv.setImageTintList(ColorStateList.valueOf(tintColorOn));
        ambulance_ll.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_yellow));
        ambulance_tv.setTextColor(ContextCompat.getColor(this, them_color));
        ambulance_iv.setImageTintList(ColorStateList.valueOf(tintColorOn));
    }

    @Override
    public void onClickLocation(Double lat, Double lon) {
        // Create a URI string for the location
        String uri = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon;
        // Create an intent to view the URI in a map application
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps"); // Use Google Maps app

        // Check if there is a Maps app to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Optionally handle the case where there is no map app
        }
//        Intent i = new Intent(PublicAgencyDirActivity.this, MapsActivity.class);
//        Bundle b = new Bundle();
//        b.putDouble("lat", lat);
//        b.putDouble("lon", lon);
//        i.putExtras(b);
//        startActivity(i);
    }

    @Override
    public void onCall(String phone) {
        callDialog(phone);
    }

    @Override
    public void onCallWebsite(String url) {
        Intent i = new Intent(PublicAgencyDirActivity.this, WebViewActivity.class);
        i.putExtra("companylink", url);
        startActivity(i);
    }

    @Override
    public void onShare(String msg, String name, String adress, String phone) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg + "\n\n" + name + "\n" + "Phone: " + phone + "\n" + adress + "\n\n" + "https://play.google.com/store/apps/details?id=com.app_neighbrsnook" + "\n\n" + "https://neighbrsnook.com/");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void callDialog(String s) {
        RecyclerView rv;
        TextView confirm, cancel, phone;
        phone_dialog = new Dialog(this);
        phone_dialog.setContentView(R.layout.cll_dialog);
        confirm = phone_dialog.findViewById(R.id.confirm_textview);
        cancel = phone_dialog.findViewById(R.id.cancel_textview);
        phone = phone_dialog.findViewById(R.id.phone);
        phone.setText(s);
        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent sIntent = new Intent(Intent.ACTION_CALL);
                sIntent.setData(Uri.parse("tel:" + s));
                sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(sIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
                phone_dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_dialog.cancel();
            }
        });
        phone_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        phone_dialog.setCancelable(true);
        phone_dialog.show();
    }
}