package com.app_neighbrsnook.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.Reaction;

import java.util.ArrayList;
import java.util.List;

public class ReactionAdapter extends RecyclerView.Adapter<ReactionAdapter.ViewHolder> {
    Dialog dialog;
    ReactionAdapter.ReactionInterface reactionInterface;
    List<Reaction> reactionList = new ArrayList<>();

    public ReactionAdapter( List<Reaction> reactionList, ReactionInterface reactionInterface) {
        this.reactionInterface =reactionInterface;
        this.reactionList = reactionList;
    }

    @NonNull
    @Override
    public ReactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reaction_row, parent, false);
        return new ReactionAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ReactionAdapter.ViewHolder holder, int position) {
        holder.reactButton.setText(reactionList.get(position).getReactionCode());
        holder.reactButton.setOnClickListener(v -> {
            reactionInterface.onClick(position, reactionList.get(position).getReactionName());

        });
    }
    @Override
    public int getItemCount() {
        return reactionList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView reactButton;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reactButton = itemView.findViewById(R.id.reactButton);
//            cb = itemView.findViewById(R.id.tik_iv);
            linearLayout = itemView.findViewById(R.id.root);
        }
    }
    public interface ReactionInterface{
        void onClick(int pos, String categoryName);
    }

}