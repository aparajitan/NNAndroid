package com.app_neighbrsnook.fragment;

import static android.Manifest.permission.CALL_PHONE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app_neighbrsnook.PublicAgencyDirActivity;
import com.app_neighbrsnook.R;

public class EmergencyFragment extends Fragment {
    TextView ambulance_rlambulance_tv;
    Dialog phone_dialog;
    ImageView searchImageView, addImageView, back_btn;
    TextView titleTv;
    Context context = getActivity();
    RelativeLayout ambulance_rl, public_agencey_dir_rl,police_rl,fire_rl,notify_contact_rl;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        titleTv = view.findViewById(R.id.title);
        back_btn= view.findViewById(R.id.back_btn);
        titleTv.setText("Emergency");
        searchImageView = view.findViewById(R.id.search_imageview);
        addImageView = view.findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);
        back_btn.setVisibility(View.GONE);
        ambulance_rl = view.findViewById(R.id.ambulance_rl);
        public_agencey_dir_rl = view.findViewById(R.id.public_agencey_dir_rl);
        police_rl = view.findViewById(R.id.police_rl);
        fire_rl = view.findViewById(R.id.fire_rl);
        notify_contact_rl = view.findViewById(R.id.notify_contact_rl);
        ambulance_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog("102", "Ambulance");          }
        });

        police_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog("100", "Police");          }
        });

        fire_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog("101", "Fire station");          }
        });

        notify_contact_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog("9015667466", "Notifiy contact");
                }
        });

        public_agencey_dir_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(getActivity(), PublicAgencyDirActivity.class);
            startActivity(i);
            }
        });
        return view;
    }

    private void callDialog(String s, String call_name) {
        RecyclerView rv;
        TextView confirm, cancel,phone, call_name_tv;
        phone_dialog = new Dialog(getActivity());
        phone_dialog.setContentView(R.layout.cll_dialog);
        call_name_tv = phone_dialog.findViewById(R.id.call_name_tv);
        confirm = phone_dialog.findViewById(R.id.confirm_textview);
        cancel = phone_dialog.findViewById(R.id.cancel_textview);
        phone = phone_dialog.findViewById(R.id.phone);
        phone.setText(s);
        phone.setTextColor(Color.parseColor("#ffcc0000"));
        call_name_tv.setVisibility(View.VISIBLE);
        call_name_tv.setText(call_name);
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
}