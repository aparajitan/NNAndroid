package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.Nbdatum;

import java.util.ArrayList;
import java.util.List;

public class BusinessCategoryAdapter2 extends RecyclerView.Adapter<BusinessCategoryAdapter2.ViewHolder> {
    Dialog dialog;
    CategoryInterface categoryInterface;
    List<Nbdatum> emailsList = new ArrayList<>();
    public BusinessCategoryAdapter2(Dialog _dialog, CategoryInterface categoryInterface, List<Nbdatum> emailsList) {
        dialog = _dialog;
        this.categoryInterface =categoryInterface;
        this.emailsList = emailsList;
    }

    public BusinessCategoryAdapter2(CategoryInterface categoryInterface, List<Nbdatum> emailsList) {
        this.categoryInterface =categoryInterface;
        this.emailsList = emailsList;
    }
    @NonNull
    @Override
    public BusinessCategoryAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false);
        return new BusinessCategoryAdapter2.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BusinessCategoryAdapter2.ViewHolder holder, int position) {
        holder.days_tv.setText(emailsList.get(position).getMember_title());
        holder.cb.setVisibility(View.GONE);
        holder.days_tv.setOnClickListener(v -> {
            categoryInterface.onClick3(emailsList.get(position).getMember_title(), emailsList.get(position).getId());
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
    public interface CategoryInterface{
        void onClick3(String categoryName, String id);
    }

//    public void filterList(ArrayList<Nbdatum> filterdNames) {
//        this.emailsList = filterdNames;
//        notifyDataSetChanged();
//    }
}