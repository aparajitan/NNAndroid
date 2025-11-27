package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.Nbdatum;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> implements Filterable {
    private List<Nbdatum> countries;
    String type;
    OnItemSelected itemSelected;

    public CountryAdapter(List<Nbdatum> countries, String type, OnItemSelected itemSelected) {
        this.countries = countries;
        this.type = type;
        this.itemSelected = itemSelected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(type.equals("country")) {
            holder.name.setText(countries.get(position).getCountryname());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemSelected.onSelected(position,countries.get(position).getCountryname(), countries.get(position).getId(), type);
                }
            });
        }

        if(type.equals("city")) {
            holder.name.setText(countries.get(position).getCity_name());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemSelected.onSelected(position,countries.get(position).getCity_name(), countries.get(position).getId(), type);
                }
            });
        }
        if(type.equals("state")) {
            holder.name.setText(countries.get(position).getStateName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemSelected.onSelected(position,countries.get(position).getStateName(), countries.get(position).getId(), type);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

    public interface  OnItemSelected{
        void onSelected(int position,String name, String id, String type);
    }

    public void filterList(ArrayList<Nbdatum> filterdNames) {
        this.countries = filterdNames;
        notifyDataSetChanged();
    }


}