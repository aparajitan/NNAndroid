package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.pojo.Nbdatum;

import java.util.ArrayList;
import java.util.List;

public class SelectPostTypeAdapter extends RecyclerView.Adapter<SelectPostTypeAdapter.ViewHolder> {

    Dialog dialog;
    CategoryInterface categoryInterface;
    List<Nbdatum> emailsList = new ArrayList<>();
    Context context;
//    String[] postType={"Carpooling","Donate or Sell","General","Lost & Founde","Safety"};
//    List<String> emailsList = Arrays.asList(postType);
    public SelectPostTypeAdapter(List<Nbdatum> emailsList,Dialog _dialog, CategoryInterface categoryInterface) {
        dialog = _dialog;
        this.categoryInterface =categoryInterface;
        this.context = context;
        this.emailsList = emailsList;

    }

    @NonNull
    @Override
    public SelectPostTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_select_post_type, parent, false);
        return new SelectPostTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPostTypeAdapter.ViewHolder holder, int position) {
        holder.days_tv.setText(emailsList.get(position).getPost_title());
        holder.cb.setVisibility(View.GONE);
        holder.days_tv.setOnClickListener(v -> {
            categoryInterface.onClickPostType(Integer.parseInt(emailsList.get(position).getId()), emailsList.get(position).getPost_title());
        });
    }

    @Override
    public int getItemCount() {
        return emailsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView days_tv;
        CheckBox cb;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            days_tv = itemView.findViewById(R.id.days_tv);
            cb = itemView.findViewById(R.id.checkbox);
            linearLayout = itemView.findViewById(R.id.root);
        }
    }

    public interface CategoryInterface{
        void onClickPostType(int id, String categoryName );
    }


}

