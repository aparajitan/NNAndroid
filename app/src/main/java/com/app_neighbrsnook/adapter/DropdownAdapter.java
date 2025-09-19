package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.StateDropdownPojo;
import java.util.ArrayList;
public class DropdownAdapter extends RecyclerView.Adapter<DropdownAdapter.DropdownHolder> {
     Context context;
     ArrayList<StateDropdownPojo> dropdownPOJOS;
     OnItemSelected itemSelected;

    public DropdownAdapter(Context context, ArrayList<StateDropdownPojo> dropdownPOJOS, OnItemSelected itemSelected) {
        this.context = context;
        this.dropdownPOJOS = dropdownPOJOS;
        this.itemSelected = itemSelected;
        isFirstTime=true;
    }
    public interface  OnItemSelected{
         void onSelected(int position,StateDropdownPojo dropdownPOJO);
     }
    @NonNull
    @Override
    public DropdownHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.inflate_dropdown_list,parent,false);
        return new DropdownHolder(view);
    }
    int selectedPosition=0;
    Boolean isFirstTime=true;
    @Override
    public void onBindViewHolder(@NonNull DropdownHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (dropdownPOJOS.get(position).stateName!=null) {
            holder.tv_name.setText(dropdownPOJOS.get(position).stateName);
        }else   if (dropdownPOJOS.get(position).cityteName!=null) {
            holder.tv_name.setText(dropdownPOJOS.get(position).cityteName);
        }
        if (!isFirstTime && selectedPosition==position){
            holder.iv_selected.setVisibility(View.VISIBLE);
            holder.iv_unselected.setVisibility(View.GONE);
        }else {
            holder.iv_selected.setVisibility(View.GONE);
            holder.iv_unselected.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFirstTime=false;
                selectedPosition=position;
                itemSelected.onSelected(position,dropdownPOJOS.get(position));
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dropdownPOJOS.size();
    }

    public class DropdownHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView iv_selected;
        ImageView iv_unselected;

        public DropdownHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_selected=itemView.findViewById(R.id.iv_selected);
            iv_unselected=itemView.findViewById(R.id.iv_unselected);

        }
    }
}
