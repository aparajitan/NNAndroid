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
import com.app_neighbrsnook.intreface.EmojiCallBack;
import com.app_neighbrsnook.model.wall.LikeDataModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.ViewHolder>{

    Context context;
    List<LikeDataModel> likeDataModels = new ArrayList<>();
    EmojiCallBack ecb;
    int[] images_emoji = {R.drawable.ic_happy, R.drawable.ic_heart, R.drawable.ic_like, R.drawable.ic_angry};

    public LikeListAdapter(List<LikeDataModel> likeDataModels) {
        this.likeDataModels = likeDataModels;
        this.ecb = ecb;
    }

    @NonNull
    @Override
    public LikeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_list_low, parent, false);
        return new LikeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeListAdapter.ViewHolder holder, int position) {
        holder.image_view.setImageResource(R.drawable.ic_candy);
        holder.user_name_tv.setText(likeDataModels.get(position).getUsername());
        if (likeDataModels.get(position).getUserpic().isEmpty()) {
            holder.userLikesPhoto.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(likeDataModels.get(position).getUserpic()).fit().centerCrop().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                    .into(holder.userLikesPhoto);
        }
        if (position == likeDataModels.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
}
    }

    @Override
    public int getItemCount() {
        return likeDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View divider;
        TextView user_name_tv;
        ImageView image_view;
        CircularImageView userLikesPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.view_divider);
            userLikesPhoto= itemView.findViewById(R.id.userLikesPhoto);
            image_view= itemView.findViewById(R.id.image_view);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
        }
    }
}
