package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.Nbdatum;

import java.util.ArrayList;
import java.util.List;

public class SkipScreenLoveMyNghAdapter extends RecyclerView.Adapter<SkipScreenLoveMyNghAdapter.ViewHolder> {
    Dialog dialog;
    LoveMyNeighbrhood categoryInterface;
    List<Nbdatum> emailsList = new ArrayList<>();
    public SkipScreenLoveMyNghAdapter(Dialog _dialog, LoveMyNeighbrhood categoryInterface, List<Nbdatum> emailsList) {
        dialog = _dialog;
        this.categoryInterface =categoryInterface;
        this.emailsList = emailsList;
    }

    public SkipScreenLoveMyNghAdapter(LoveMyNeighbrhood categoryInterface, List<Nbdatum> emailsList) {
        this.categoryInterface =categoryInterface;
        this.emailsList = emailsList;
    }
    @NonNull
    @Override
    public SkipScreenLoveMyNghAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false);
        return new SkipScreenLoveMyNghAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SkipScreenLoveMyNghAdapter.ViewHolder holder, int position) {
       // holder.days_tv.setText(emailsList.get(position).getBusiness_title());
    /*    holder.days_tv.setText(emailsList.get(position).getMember_title());
        holder.cb.setVisibility(View.VISIBLE);
        holder.days_tv.setOnClickListener(v -> {
            //categoryInterface.onClick(emailsList.get(position).getBusiness_title(), emailsList.get(position).getId());
            categoryInterface.onClick2(emailsList.get(position).getMember_title(), emailsList.get(position).getId());
        });*/
        Log.e("dfadfs",new Gson().toJson(emailsList.get(position)));
        holder.days_tv.setText(emailsList.get(position).getMember_title());
        holder.cb.setVisibility(View.VISIBLE);
        if (emailsList.get(position).isSelected()){
            holder.cb.setImageResource(R.drawable.right_icon_update);
        }else {
            holder.cb.setImageResource(R.drawable.img_selector);

        }
        holder.itemView.setOnClickListener(v -> {
            Log.e("fdsadfsd","Click hua h");
            //categoryInterface.onClick(emailsList.get(position).getBusiness_title(), emailsList.get(position).getId());
            //  categoryInterface.onClick1(emailsList.get(position).getMember_title(), emailsList.get(position).getId());
            //    holder.cb.setImageResource(R.drawable.img_selector);
            emailsList.get(position).setSelected(!emailsList.get(position).isSelected());
            notifyDataSetChanged();/**/
        });

    }
    @Override
    public int getItemCount() {
        return emailsList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView days_tv;
        ImageView cb;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            days_tv = itemView.findViewById(R.id.days_tv);
            cb = itemView.findViewById(R.id.tik_iv);
            linearLayout = itemView.findViewById(R.id.root);
        }
    }
    public interface LoveMyNeighbrhood{
        void onClick2(String categoryName, String id);
    }

//    public void filterList(ArrayList<Nbdatum> filterdNames) {
//        this.emailsList = filterdNames;
//        notifyDataSetChanged();
//    }
}