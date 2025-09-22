package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
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
import com.app_neighbrsnook.model.OfferModel;

import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    List<OfferModel> nearbyList = new ArrayList<>();
    Context mcon;
    CategoryCallBack ccb;

    public CheckoutAdapter(List<OfferModel> nearbyList, Context mContext, CategoryCallBack ccb) {
        this.nearbyList = nearbyList;
        this.mcon = mContext;
        this.ccb = ccb;
    }

    @NonNull
    @Override
    public CheckoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item_row, parent, false);
        mcon = parent.getContext();
        return new CheckoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(nearbyList.get(position).getName());
        holder.name.getResources().getColor(R.color.white);
        holder.porduct_image.setImageResource(nearbyList.get(position).getBg_color());
        holder.percent.setText(nearbyList.get(position).getOffer_percentage());
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

        TextView name, percent,item_name, time_textview;
        ImageView porduct_image;
        RelativeLayout root;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            porduct_image = itemView.findViewById(R.id.porduct_image);
            cardView = itemView.findViewById(R.id.cardView);
            root = itemView.findViewById(R.id.root);
            percent = itemView.findViewById(R.id.percent);

        }
    }

}