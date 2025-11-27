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

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.Holder> {
    Dialog dialog;

    public InterestAdapter(Dialog dialog, CategoryInterface1 categoryInterface1) {
        this.dialog = dialog;
        this.categoryInterface1 = categoryInterface1;
    }

    CategoryInterface1 categoryInterface1;


    String[] interest = {"Astronaut", "Business", "Banker","CA","Cricketer","Doctor","Entrepreneur","Home Maker","Other","HR","Police","Retired","Web Designer",
    "Sales & Marketing","Student"};
    List<String> emailsList = Arrays.asList(interest);

    @NonNull
    @Override
    public InterestAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interes_layout, parent, false);
        return new InterestAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestAdapter.Holder holder, int position) {
        holder.days_tv.setText(emailsList.get(position));
        holder.cb.setVisibility(View.GONE);
        holder.days_tv.setOnClickListener(v -> {
            categoryInterface1.onclick1(emailsList.get(position));

           // categoryInterface1.onClick(emailsList.get(position));
        });
    }
    @Override
    public int getItemCount() {
        return emailsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView days_tv;
        ImageView cb;
        LinearLayout linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);


            days_tv = itemView.findViewById(R.id.days_tv);
            cb = itemView.findViewById(R.id.tik_iv);
            linearLayout = itemView.findViewById(R.id.root);
        }
    }


    public interface CategoryInterface1 {
        void onclick1(String categoryName);
    }
}
