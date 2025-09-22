package com.app_neighbrsnook.fragment;

import static android.Manifest.permission.CALL_PHONE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.HospitalAdapter;
import com.app_neighbrsnook.model.HospitalModel;

import java.util.ArrayList;
import java.util.List;

public class HospitalFragment extends Fragment implements View.OnClickListener{

    TextView p3,p4,p5,p6,p7,p8, titleTv;
    Dialog phone_dialog;
    ImageView back_btn;
Context context;
    RecyclerView hospital_rv;
    HospitalAdapter hospitalAdapter;
    List<HospitalModel> hospitalList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);
        hospital_rv = view.findViewById(R.id.hospital_rv);
        hospital_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        hospitalAdapter = new HospitalAdapter((ArrayList<HospitalModel>) getData(), HospitalFragment.this);
//        hospital_rv.setAdapter(hospitalAdapter);
//        titleTv = view.findViewById(R.id.title);
//        back_btn = view.findViewById(R.id.back_btn);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            getActivity().onBackPressed();
        }
    }

    private void callDialog(String s) {
        RecyclerView rv;
        TextView confirm, cancel,phone;
        phone_dialog = new Dialog(getActivity());
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
                sIntent.setData(Uri.parse("tel:"+s));
//                dail.setData(Uri.parse("tel:"+station_Number));
                sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    getActivity().startActivity(sIntent);
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
        phone_dialog.setCancelable(false);
        phone_dialog.show();


    }

    private List<HospitalModel> getData()
    {
        hospitalList.add(new HospitalModel("Max Multi Speciality Centre", "Noida, A364, A Block, Pocket A, Sector 19, Noida, Uttar Pradesh 201301", 28.57987, 77.32207));
        hospitalList.add(new HospitalModel("Apollo Hospital", "E 2, Rd, Block E, Sector 26, Noida, Uttar Pradesh 201301", 28.580332781348464, 77.33524870704757));
        hospitalList.add(new HospitalModel("Kailash Hospital", " Noida, A364, A Block, Pocket A, Sector 19, Noida, Uttar Pradesh 201301", 28.578801150460865, 77.3319334211903));
        hospitalList.add(new HospitalModel("City Hospital", "A-42, Shaheen Bagh, Block A, Jamia Nagar, Okhla, New Delhi, Delhi 110025", 28.550774018637302, 77.30038183033494));
        return hospitalList;
    }

//    @Override
//    public void onClickLocation(Double lat, Double lon) {
//        MapsActivity mapsActivity;
//
//        Intent i = new Intent(getActivity(), MapsActivity.class);
//        Bundle b = new Bundle();
//        b.putDouble("lat", lat);
//        b.putDouble("lon", lon);
//        i.putExtras(b);
//        startActivity(i);
//
//    }

//    @Override
//    public void onCall(String phone) {
//        callDialog(phone);
//    }
//
//    @Override
//    public void onShare(int pos) {

//    }
}