package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.nearBy.NearByBusinessActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.BusinessListModel;

import java.util.ArrayList;
import java.util.List;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.ViewHolder> {

    List<BusinessListModel> nearbyList = new ArrayList<>();
    Context mcon;
    Activity activity;

    public BusinessListAdapter(List<BusinessListModel> nearbyList) {
        this.nearbyList = nearbyList;
    }

    @NonNull
    @Override
    public BusinessListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list_item_row, parent, false);
        mcon = parent.getContext();
        return new BusinessListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(nearbyList.get(position).getName());
        holder.category_image.setImageResource(nearbyList.get(position).getImg());
        holder.rating_tv.setText(nearbyList.get(position).getRating());
        holder.Location_tv.setText(nearbyList.get(position).getLocation());
        holder.distance_tv.setText(nearbyList.get(position).getDistance());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ccb.categoryOnClick(position, nearbyList.get(position).getName());

                Intent i = new Intent(mcon, NearByBusinessActivity.class);
                mcon.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, percent,rating_tv, Location_tv, review_tv, distance_tv;
        ImageView category_image;
        LinearLayout root;
        CardView cardView;
        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.business_name_tv);
            category_image = itemView.findViewById(R.id.category_image);
            cardView = itemView.findViewById(R.id.cardView);
            root = itemView.findViewById(R.id.root);
            rating_tv = itemView.findViewById(R.id.rating_tv);
            Location_tv = itemView.findViewById(R.id.Location_tv);
            distance_tv = itemView.findViewById(R.id.distance_tv);

        }
    }

}