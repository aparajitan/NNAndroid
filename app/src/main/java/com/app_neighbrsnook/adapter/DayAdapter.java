package com.app_neighbrsnook.adapter;

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
import com.app_neighbrsnook.pojo.Day;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    List<Day> dayList = new ArrayList<>();
    private ArrayList<Boolean> selectedItems;
    DayInterface dayInterface;
    private final ArrayList<String> closingday = new ArrayList<>();
    private boolean isSelected;
    private  StringBuilder builder;

    public DayAdapter(DayInterface dayInterface, List<Day> dayList, StringBuilder builder) {
        this.dayList = dayList;
        this.dayInterface = dayInterface;
        this.builder = (builder != null) ? builder : new StringBuilder();
    }

    @NonNull
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false);
        return new DayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.ViewHolder holder, int position) {

        Day currentDay = dayList.get(position);
        holder.days_tv.setText(currentDay.getName());

        if (builder.toString().contains(currentDay.getName())) {
            currentDay.setChecked(true);
        } else {
            currentDay.setChecked(false);
        }

        if (currentDay.isChecked()) {
            holder.cb.setImageResource(R.drawable.right_icon_update);
        } else {
            holder.cb.setImageResource(R.drawable.img_selector);
        }

        holder.itemView.setOnClickListener(v -> {
            boolean isChecked = !currentDay.isChecked();
            currentDay.setChecked(isChecked);

            if (isChecked) {
                if (!builder.toString().contains(currentDay.getName())) {
                    builder.append(currentDay.getName()).append(", ");
                }
            } else {
                String updatedString = builder.toString().replace(currentDay.getName() + ", ", "");
                builder.setLength(0);
                builder.append(updatedString);
            }

            notifyItemChanged(position);
        });

//        holder.days_tv.setText(dayList.get(position).getName());
//        holder.cb.setVisibility(View.VISIBLE);
//        if (dayList.get(position).isChecked()) {
//            holder.cb.setImageResource(R.drawable.right_icon_update);
//        } else {
//            holder.cb.setImageResource(R.drawable.img_selector);
//        }
//        holder.itemView.setOnClickListener(v -> {
//            dayList.get(position).setChecked(!dayList.get(position).isChecked());
//            notifyDataSetChanged();
//        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
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

    public interface DayInterface {
        void onClick(String categoryName, String id);
}
}