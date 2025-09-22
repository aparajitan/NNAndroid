package com.app_neighbrsnook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.wall.Emojilistdatum;
import com.app_neighbrsnook.utils.EmojiUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>{

    private final Context context;
    private final List<Emojilistdatum> favoriteDataModels;

    public FavoriteListAdapter(Context context, List<Emojilistdatum> favoriteDataModels) {
        this.context = context;
        this.favoriteDataModels = favoriteDataModels;
    }

    @NonNull
    @Override
    public FavoriteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_list_low, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListAdapter.ViewHolder holder, int position) {
        Emojilistdatum data = favoriteDataModels.get(position);
        String emoji = data.getEmojiunicode();
        try {
            Bitmap emojiBitmap = EmojiUtil.getEmojiBitmap(context, emoji, 20);
            holder.image_view.setImageBitmap(emojiBitmap);
        } catch (IllegalArgumentException e) {
            holder.image_view.setImageResource(R.drawable.baseline_favorite_24);
        }
        holder.user_name_tv.setText(data.getUsername());

        if (favoriteDataModels.get(position).getUserpic().isEmpty()) {
            holder.userLikesPhoto.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(favoriteDataModels.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                    .into(holder.userLikesPhoto);
        }
        if (position == favoriteDataModels.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
}
    }

    @Override
    public int getItemCount() {
        return favoriteDataModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name_tv;
        ImageView image_view;
        CircularImageView userLikesPhoto;
        View divider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.view_divider);
            userLikesPhoto = itemView.findViewById(R.id.userLikesPhoto);
            image_view = itemView.findViewById(R.id.image_view);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
        }
    }
}
