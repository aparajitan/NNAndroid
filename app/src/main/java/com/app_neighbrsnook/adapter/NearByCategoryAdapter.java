package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.BusinessListActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.intreface.CategoryCallBack;
import com.app_neighbrsnook.model.PopularModel;

import java.util.ArrayList;
import java.util.List;

public class NearByCategoryAdapter  extends RecyclerView.Adapter<NearByCategoryAdapter.ViewHolder> {

    List<PopularModel> nearbyList = new ArrayList<>();
    Context mcon;
    CategoryCallBack ccb;

    public NearByCategoryAdapter(List<PopularModel> nearbyList, Context mContext, CategoryCallBack ccb) {
        this.nearbyList = nearbyList;
        this.mcon = mContext;
        this.ccb = ccb;
    }

    @NonNull
    @Override
    public NearByCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.near_by_category_row, parent, false);
        mcon = parent.getContext();
        return new NearByCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByCategoryAdapter.ViewHolder holder, int position) {

        holder.name.setText(nearbyList.get(position).getName());
        holder.name.getResources().getColor(R.color.white);
        holder.cardView.setCardBackgroundColor(nearbyList.get(position).getBg_color());
        holder.image.setBackgroundResource(nearbyList.get(position).getImage());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mcon, BusinessListActivity.class);
                mcon.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
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