package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.SuggestionModel;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.Holder> {
    private final List<SuggestionModel> suggestionList;
    SuggestionAdapter.NewRequest newRequest;
    public SuggestionAdapter(List<SuggestionModel> suggestionList, NewRequest newRequest) {
        this.suggestionList = suggestionList;
        this.newRequest = newRequest;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_adapter_layout,parent,false);
        return new SuggestionAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        SuggestionModel suggestionModel = suggestionList.get(position);
        holder.tv_business.setText(suggestionModel.getBusiness_title());
        holder.tv_address.setText(suggestionModel.getAddress_type());
        holder.lnr_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRequest.onDetailPage(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return suggestionList.size();
    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_business,tv_address;
        LinearLayout lnr_root;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_business=itemView.findViewById(R.id.tv_business);
            tv_address=itemView.findViewById(R.id.tv_address);
            lnr_root=itemView.findViewById(R.id.lnr_root_suggestion);
        }
    }
    public interface NewRequest{
        void onClick(int pos);
        void onDetail(int pos);
        void threeDot(int pos);
        void onDetailPage(int pos);
    }
}