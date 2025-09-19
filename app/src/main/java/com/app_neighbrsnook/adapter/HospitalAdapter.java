package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.publicdirectory.Listdatum;

import java.util.ArrayList;
import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    List<Listdatum> nearbyList = new ArrayList<>();
    Context mcon;
    boolean show = true;
    private Animation animationUp;
    private Animation animationDown;
    LocationInterface locationInterface;

    public HospitalAdapter(List<Listdatum> nearbyList, LocationInterface locationInterface) {
        this.nearbyList = nearbyList;
        this.locationInterface = locationInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_row, parent, false);
        mcon = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        animationUp = AnimationUtils.loadAnimation(mcon.getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(mcon.getApplicationContext(), R.anim.slide_down);
        holder.title_tv.setText(nearbyList.get(position).getName());
        holder.desc_tv.setText(nearbyList.get(position).getAddress());
//        holder.call1_tv.setText(nearbyList.get(position).getNumber1());
        holder.txtv_website.setText(nearbyList.get(position).getWebsite());
        if (nearbyList.get(position).getNumber1() != null && !nearbyList.get(position).getNumber1().equals("")) {
            holder.call1_rl.setVisibility(View.VISIBLE);
            holder.call1_tv.setText(nearbyList.get(position).getNumber1());
        }
        if (nearbyList.get(position).getNumber2() != null && !nearbyList.get(position).getNumber2().equals("")) {
            holder.call2_rl.setVisibility(View.VISIBLE);
            holder.call2_tv.setText(nearbyList.get(position).getNumber2());
        }

        if (nearbyList.get(position).getType().equals("Ambulance")) {

        }

        holder.show_hide_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show) {
                    holder.ans_layout.setVisibility(View.VISIBLE);
                    holder.up_iv.setVisibility(View.VISIBLE);
                    holder.down_iv.setVisibility(View.GONE);
                    show = false;
                } else if (!show) {
                    holder.ans_layout.setVisibility(View.GONE);
                    holder.down_iv.setVisibility(View.VISIBLE);
                    holder.up_iv.setVisibility(View.GONE);
                    show = true;
                }
            }
        });

        holder.share_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = "Checkout this " + nearbyList.get(position).getType() + " shared on Neighbrsnook";

                locationInterface.onShare(msg,nearbyList.get(position).getName(), nearbyList.get(position).getAddress(), nearbyList.get(position).getNumber1());
            }
        });

        holder.call_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationInterface.onCall(nearbyList.get(position).getNumber1());
            }
        });

        holder.call_imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationInterface.onCall(nearbyList.get(position).getNumber2());
            }
        });

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationInterface.onClickLocation(Double.parseDouble(nearbyList.get(position).getLat()), Double.parseDouble(nearbyList.get(position).get_long()));
            }
        });

        holder.txtv_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationInterface.onCallWebsite(nearbyList.get(position).getWebsite());
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv, desc_tv, call_tv, call2_tv, call1_tv, txtv_website;
        LinearLayout root, ans_layout;
        ImageView down_iv, up_iv, location, call_imageview, share_imageview, call_imageview2;
        RelativeLayout show_hide_rl, call1_rl, call2_rl;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.question_tv);
            desc_tv = itemView.findViewById(R.id.ans_tv);
            root = itemView.findViewById(R.id.root);
            down_iv = itemView.findViewById(R.id.down_iv);
            ans_layout = itemView.findViewById(R.id.ans_layout);
            up_iv = itemView.findViewById(R.id.up_iv);
            show_hide_rl = itemView.findViewById(R.id.show_hide_rl);
            location = itemView.findViewById(R.id.location);
            call_imageview = itemView.findViewById(R.id.call_imageview);
            call_tv = itemView.findViewById(R.id.call_tv);
            share_imageview = itemView.findViewById(R.id.share_imageview);
            call1_rl = itemView.findViewById(R.id.call1_rl);
            call2_tv = itemView.findViewById(R.id.call2_tv);
            call1_tv = itemView.findViewById(R.id.call1_tv);
            call2_tv = itemView.findViewById(R.id.call2_tv);
            call_imageview2 = itemView.findViewById(R.id.call_imageview2);
            call2_rl = itemView.findViewById(R.id.call2_rl);
            txtv_website = itemView.findViewById(R.id.txtv_website);
        }
    }

    public interface LocationInterface {
        void onClickLocation(Double lat, Double lon);

        void onCall(String phone);

        void onShare(String msg, String name, String address, String phone);

        void onCallWebsite(String url);
//        void onAmbulanceCall(List<String> ambulanceList);
    }

}
