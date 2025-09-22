package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.CategoryCallBack;
import com.app_neighbrsnook.model.PopularModel;

import java.util.ArrayList;
import java.util.List;

public class PopularItemAdapter extends RecyclerView.Adapter<PopularItemAdapter.ViewHolder> {

    List<PopularModel> nearbyList = new ArrayList<>();
    Context mcon;
    Activity activity;
    CategoryCallBack ccb;

    public PopularItemAdapter(List<PopularModel> nearbyList, Context mContext, CategoryCallBack ccb) {
        this.nearbyList = nearbyList;
        this.mcon = mContext;
        this.ccb = ccb;
    }

    @NonNull
    @Override
    public PopularItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_row, parent, false);
        mcon = parent.getContext();
        return new PopularItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(nearbyList.get(position).getName());
        holder.name.getResources().getColor(R.color.white);
        holder.cardView.setCardBackgroundColor(nearbyList.get(position).getBg_color());
        holder.root.setBackgroundResource(nearbyList.get(position).getImage());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ccb.categoryOnClick(position, nearbyList.get(position).getName());

            }
        });

    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, product_name,item_name, time_textview;
        ImageView image;
        RelativeLayout root;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
            root = itemView.findViewById(R.id.root);

        }
    }

}