package com.app_neighbrsnook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;

import java.util.List;

public class NeighbourhoodAdapter extends RecyclerView.Adapter<NeighbourhoodAdapter.ViewHolder> {

    private List<String> neighbourhoodList;
    private List<Integer> neighbourhoodIds;
    private NeighbourhoodInterface listener;

    public NeighbourhoodAdapter(List<String> neighbourhoodList, List<Integer> neighbourhoodIds, NeighbourhoodInterface listener) {
        this.neighbourhoodList = neighbourhoodList;
        this.neighbourhoodIds = neighbourhoodIds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NeighbourhoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false); // reuse day_list layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NeighbourhoodAdapter.ViewHolder holder, int position) {
        holder.days_tv.setText(neighbourhoodList.get(position));
        holder.cb.setVisibility(View.GONE);

        holder.days_tv.setOnClickListener(v -> {
            listener.onNeighbourhoodSelected(neighbourhoodList.get(position), neighbourhoodIds.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return neighbourhoodList.size();
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

    public interface NeighbourhoodInterface {
        void onNeighbourhoodSelected(String name, int id);
    }
}

