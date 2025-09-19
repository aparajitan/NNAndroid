package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;
public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.Holder> {
    Dialog dialog;
    GenderInterface categoryInterface;
    String[] days = {"Male","Female","Other"};
    List<String> emailsList = Arrays.asList(days);

    public GenderAdapter(Dialog dialog, GenderInterface categoryInterface, String[] days, List<String> emailsList) {
        this.dialog = dialog;
        this.categoryInterface = categoryInterface;
        this.days = days;
        this.emailsList = emailsList;
    }
    @NonNull
    @Override
    public GenderAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull GenderAdapter.Holder holder, int position) {
    }
    @Override
    public int getItemCount() {
        return 0;
    }
    public class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public interface GenderInterface{
        void onClick(String categoryName );
    }
}