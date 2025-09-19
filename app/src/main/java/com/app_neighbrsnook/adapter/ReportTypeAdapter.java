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
import com.app_neighbrsnook.pojo.reportModule.Listdatum;

import java.util.ArrayList;
import java.util.List;

public class ReportTypeAdapter extends RecyclerView.Adapter<ReportTypeAdapter.ViewHolder> {
    Dialog dialog;
    ReportTypeAdapter.CategoryInterface categoryInterface;
    List<Listdatum> emailsList = new ArrayList<>();

    public ReportTypeAdapter( ReportTypeAdapter.CategoryInterface categoryInterface, List<Listdatum> emailsList) {
        this.categoryInterface = categoryInterface;
        this.emailsList = emailsList;
    }

    @NonNull
    @Override
    public ReportTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false);
        return new ReportTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportTypeAdapter.ViewHolder holder, int position) {
        holder.days_tv.setText(emailsList.get(position).getName());
        holder.cb.setVisibility(View.GONE);
        holder.days_tv.setOnClickListener(v -> {
            categoryInterface.onClick(emailsList.get(position).getName(), emailsList.get(position).getId());

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

    public interface CategoryInterface {
        void onClick(String categoryName, String id);
    }
}