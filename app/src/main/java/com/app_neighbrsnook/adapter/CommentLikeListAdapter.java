package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.wall.Emojilistdatum;

import java.util.List;

public class CommentLikeListAdapter extends RecyclerView.Adapter<CommentLikeListAdapter.ViewHolder>{

    private final Context context;
    private final List<Emojilistdatum> favoriteDataModels;

    public CommentLikeListAdapter(Context context, List<Emojilistdatum> favoriteDataModels) {
        this.context = context;
        this.favoriteDataModels = favoriteDataModels;
    }

    @NonNull
    @Override
    public CommentLikeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_list_low, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentLikeListAdapter.ViewHolder holder, int position) {
        Emojilistdatum data = favoriteDataModels.get(position);
        holder.image_view.setImageResource(R.drawable.ic_baseline_favorite_red_24);
        holder.user_name_tv.setText(data.getUsername());
    }

    @Override
    public int getItemCount() {
        return favoriteDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name_tv;
        ImageView image_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.image_view);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
        }
    }
}
