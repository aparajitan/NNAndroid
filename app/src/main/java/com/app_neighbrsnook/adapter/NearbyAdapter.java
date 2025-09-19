package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.neighbrhood.NearbyModel;

import java.util.ArrayList;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    List<NearbyModel> nearbyList = new ArrayList<>();
    Activity activity;
    Context mcon;
    Intent memberIntent;
    public NearbyAdapter(List<NearbyModel> nearbyList) {
        this.nearbyList = nearbyList;
    }
    @NonNull
    @Override
    public NearbyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_row, parent, false);
        mcon = parent.getContext();
        return new NearbyAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.name_tv.setText(nearbyList.get(position).getName());
//        holder.category_name_iv.setImageResource(nearbyList.get(position).getImage());
//        holder.count_tv.setText(nearbyList.get(position).getCount());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            holder.count_tv.setTextColor(mcon.getColor(nearbyList.get(position).getColor()));
//        }
//
//        holder.category_name_iv.setBackgroundResource(nearbyList.get(position).getDrawable());
//        holder.root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (nearbyList.get(position).getName())
//                {
//                    case "Members":
//                        break;
//                    case "Groups":
//                        memberIntent = new Intent(mcon, GroupListsScreensAll.class) ;
//                        break;
//                    case "Events":
//                        memberIntent = new Intent(mcon, EventShowAll.class) ;
//                        break;
//                    case "Polls":
//                        memberIntent = new Intent(mcon, PollActivity.class) ;
//                        break;
//                    case "Business":
//                        memberIntent = new Intent(mcon, BusinessActivity.class) ;
//                        break;
//                    case "Suggestions":
//                        memberIntent = new Intent(mcon, SuggestionActivity.class) ;
//                        break;
//                }
//
//                mcon.startActivity(memberIntent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView category_name_iv;
        TextView count_tv, name_tv;
        CheckBox cb;
        LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name_iv = itemView.findViewById(R.id.category_name_iv);
            count_tv = itemView.findViewById(R.id.count_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            root = itemView.findViewById(R.id.root);
        }
    }

}
