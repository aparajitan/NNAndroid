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
import java.util.Arrays;
import java.util.List;

public class SuggestionTypeCategory extends RecyclerView.Adapter<SuggestionTypeCategory.ViewHolder> {

    Dialog dialog;
    CategoryInterface categoryInterface;

    String[] days = {"Cafe & Bakery","Restaurants", "Day Care","Pet Care", "Garden & Nursery",
            "Hospital & Clinics", "Plumber","Carpenter","Electrician",
            "School","Tuition","Groceries", "Sallon","Fitness"};
    List<String> emailsList = Arrays.asList(days);
    public SuggestionTypeCategory(Dialog _dialog, CategoryInterface categoryInterface) {
        dialog = _dialog;
        this.categoryInterface =categoryInterface;
    }
    @NonNull
    @Override
    public SuggestionTypeCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_select_category, parent, false);
        return new SuggestionTypeCategory.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SuggestionTypeCategory.ViewHolder holder, int position) {
        holder.days_tv.setText(emailsList.get(position));
        holder.cb.setVisibility(View.VISIBLE);
        holder.days_tv.setOnClickListener(v -> {
            categoryInterface.onClick(emailsList.get(position));
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
        void onClick(String categoryName );
    }
}